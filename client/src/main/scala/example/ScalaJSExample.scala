package example

import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.{Binding, dom}
import example.services.MyClient
import org.scalajs.dom.document
import org.scalajs.dom.raw.{Event, HTMLElement}
import api.CountAPI
import autowire._

import scala.scalajs.js

import scala.concurrent.ExecutionContext.Implicits.global

object ScalaJSExample {

  implicit def makeIntellijHappy(x: scala.xml.Elem): Binding[HTMLElement] = ???

  /**
    * Ajax Request to server, updates data state with number
    * of requests to count.
    * @param data
    */
  def countRequest(data: Var[String]): Unit = {
    MyClient[CountAPI].count().call().foreach(c => data.value = c.toString)
  }

  @dom
  def render = {
    val data = Var("")
    countRequest(data) // initial population
    <div>
      <button onclick={event: Event => countRequest(data) }>
        Boop
      </button>
      From Play: The server has been booped { data.bind } times. Shared Message: {shared.SharedMessages.itWorks}.
    </div>
  }

  def main(args: Array[String]): Unit = {
    println("Here")
    dom.render(document.body, render)
  }

}
