package autowire

import upickle.Js
import upickle.default._

object AutowireServer extends Server[Js.Value, Reader, Writer] {
  def write[Result: Writer](r: Result): Js.Value = writeJs(r)
  def read[Result: Reader](p: Js.Value): Result = readJs[Result](p)
}
