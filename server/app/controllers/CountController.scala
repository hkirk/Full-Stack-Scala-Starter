package controllers

import javax.inject._
import play.api.mvc._
import services.Counter
import upickle.Js

import autowire.AutowireServer
import api.CountAPI

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller demonstrates how to use dependency injection to
  * bind a component into a controller class. The class creates an
  * `Action` that shows an incrementing count to users. The [[Counter]]
  * object is injected by the Guice dependency injection system.
  */
@Singleton
class CountController @Inject()(counter: Counter,
                                components: ControllerComponents)
  extends AbstractController(components) with CountAPI {

  def api(path: String): Action[AnyContent] = Action.async { request =>
    val body = request.body.asText.getOrElse("")
    var autowireRequest = autowire.Core.Request(
      path.split("/"),
      upickle.json.read(body).asInstanceOf[Js.Obj].value.toMap
    )
    val jsValue = AutowireServer.route[CountAPI](this)(autowireRequest)

    jsValue.map(upickle.json.write(_)).map{
      body =>
        Ok(body)
    }
  }

  /**
    * Create an action that responds with the [[Counter]]'s current
    * count. The result is plain text. This `Action` is mapped to
    * `GET /count` requests by an entry in the `routes` config file.
    */
  override def count(): Future[Int] = {
    Future(counter.nextCount())
  }
}
