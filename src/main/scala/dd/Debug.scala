package dd

object Debug {

  def tree(part: Part, depth: Int = 0): Unit = {
    def p(a: Any): Unit = println(("  " * depth) + part.getClass.getSimpleName + ": " + a)
    part match {
      case r: Rule =>
        p(r.name)
        tree(r.choices, depth + 1)
      case n: Node =>
        p("")
        n.children.foreach(c => tree(c, depth + 1))
      case l: Leaf =>
        p(l.token)
    }
  }

}
