package dd

import scala.scalajs.js
import scala.scalajs.js.JSApp
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.JSExport

object Main extends JSApp {

  @JSExport
  override def main(): Unit = {}

  @JSExport
  def parseAll(rules: String): js.Array[String] =
    Parser.parseAll(rules).map(_.toDiagram).toJSArray

}
