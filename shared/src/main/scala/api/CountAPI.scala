package api

import scala.concurrent.Future

trait CountAPI {
  def count(): Future[Int]
}
