package com.mkalaimalai

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.routing.RoutedActorRef;
import akka.io.IO

import spray.can.Http
import spray.routing._

class RestApi(decider: ActorRef, exposedPort: Int) extends Actor with HttpServiceBase with ActorLogging {
  val route: Route = path("message" / Segment) { (message) =>
    get {
      complete {
        log.info(s"Request for transaction $message")
        decider.tell(message, null)
        "success"
      }
    }
  }

  def receive = runRoute(route)

  implicit val system = context.system
  IO(Http) ! Http.Bind(self, interface = "0.0.0.0", port = exposedPort)
}
