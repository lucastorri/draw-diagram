package dd

import scala.collection.mutable

object Parser {

  val RefFormat = "<(.*)>".r
  val delimiters = "[]()| \t"

  private val delimitersSet = delimiters.toSet

  def tokenize(string: String): Seq[String] = {
    val tokens = mutable.ListBuffer.empty[String]
    val buffer = mutable.StringBuilder.newBuilder

    for (c <- string) {
      if (delimitersSet(c)) {
        tokens.append(buffer.toString())
        tokens.append(c.toString)
        buffer.clear()
      } else {
        buffer.append(c)
      }
    }
    tokens.append(buffer.toString())

    tokens.map(_.trim).filter(_.nonEmpty).toSeq
  }

  def parseAll(rules: String): Seq[Part] = {
    val lines = rules
      .split("\n")
      .map(_.trim)
      .filter(_.nonEmpty)
      .map(parseLine)

    val nameIndex = lines
      .map { case (name, _) => name }
      .distinct
      .zipWithIndex
      .toMap

    lines
      .groupBy { case (name, _) => name }
      .mapValues(all => all.map { case (name, part) => part }.toSet)
      .toSeq
      .sortBy { case (name, _) => nameIndex(name) }
      .map { case (name, parts) => Rule(name, Choice(parts)) }
  }

  def parseLine(line: String): (String, Part) = {
    val Array(language, name, rule) = line.split("\t")
    s"$language:$name" -> parseRule(rule)
  }

  def parseRule(rule: String): Part = {
    parseRule(tokenize(rule))
  }

  private def parseRule(tokens: Seq[String]): Node = {
    val stack = mutable.Stack[Node](Sequence())

    tokens.foreach {
      case "[" =>
        stack.push(Optional())
        stack.push(Sequence())
      case "(" =>
        stack.push(Choice())
        stack.push(Sequence())
      case "|" =>
        val seq = stack.pop()
        val choice = stack.pop()
        stack.push(choice.add(seq))
        stack.push(Sequence())
      case "]" | ")" =>
        val seq = stack.pop()
        val node = stack.pop()
        val parent = stack.pop()
        stack.push(parent.add(node.add(seq)))
      case RefFormat(name) =>
        stack.push(stack.pop().add(Ref(name)))
      case t =>
        stack.push(stack.pop().add(Token(t)))
    }

    stack.pop()
  }

}
