package example.services

import org.scalajs.dom
import upickle.default.{readJs, writeJs}
import upickle.Js
import upickle.default._
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

//AutoWire Client
object MyClient extends autowire.Client[Js.Value, Reader, Writer]{

  override def doCall(req: Request): Future[Js.Value] = {
    println("data: " + upickle.json.write(Js.Obj(req.args.toSeq:_*)))
    dom.ext.Ajax.post(
      url = "/api/CountApi/" + req.path.mkString("/"),
      data = upickle.json.write(Js.Obj(req.args.toSeq:_*))
    ).map(_.responseText)
      .map(upickle.json.read)
  }

  def read[Result: Reader](p: Js.Value): Result = readJs[Result](p)
  def write[Result: Writer](r: Result): Js.Value = writeJs(r)
}
