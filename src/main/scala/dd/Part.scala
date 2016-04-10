package dd

sealed trait Part {

  def toDiagram: String =
    toDiagram(StringBuilder.newBuilder.append("Diagram(")).append(")").toString

  private[Part] def toDiagram(builder: StringBuilder): StringBuilder = this match {
    case Token(token) =>
      builder
        .append("Terminal('")
        .append(token)
        .append("')")
    case Ref(token) =>
      builder
        .append("NonTerminal('")
        .append(token)
        .append("')")
    case Comment(token) =>
      builder
        .append("Comment('")
        .append(token)
        .append("')")
    case Sequence(parts) =>
      builder.append("Sequence(")
      if (parts.nonEmpty) {
        parts.head.toDiagram(builder)
        parts.tail.foreach(_.toDiagram(builder.append(",")))
      }
      builder.append(")")
    case Optional(parts) =>
      builder.append("Optional(")
      parts.toDiagram(builder)
      builder.append(")")
    case Choice(options) =>
      builder.append("Choice(0")
      options.foreach(_.toDiagram(builder.append(",")))
      builder.append(")")
    case Rule(name, choices) =>
      Sequence(Seq(Comment(name), choices)).toDiagram(builder)
  }

}

sealed trait Leaf extends Part {
  def token: String
}
case class Token(token: String) extends Leaf
case class Ref(token: String) extends Leaf
case class Comment(token: String) extends Leaf

sealed trait Node extends Part {
  def add(child: Part): Node
  def children: Seq[Part]
}
case class Sequence(parts: Seq[Part] = Seq.empty) extends Node {
  override def add(child: Part): Sequence = copy(parts = parts :+ child)
  override def children: Seq[Part] = parts
}
case class Optional(parts: Sequence = Sequence()) extends Node {
  override def add(child: Part): Optional = copy(parts = parts.add(child))
  override def children: Seq[Part] = Seq(parts)
}
case class Choice(options: Set[Part] = Set.empty) extends Node {
  override def add(child: Part): Choice = copy(options = options + child)
  override def children: Seq[Part] = options.toSeq
}
case class Rule(name: String, choices: Choice) extends Part
