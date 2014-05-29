package spray.examples

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import spray.can.Http

/**
 * Main
 * @author debop created at 2014. 5. 29.
 */
object Main extends App {

    implicit val system = ActorSystem()

    // the handler actor replies to incoming HttpRequests
    val handler = system.actorOf(Props[BenchmarkService], name = "handler")

    val interface = system.settings.config.getString("app.interface")
    val port = system.settings.config.getInt("app.port")

    IO(Http) ! Http.Bind(handler, interface, port)
}
