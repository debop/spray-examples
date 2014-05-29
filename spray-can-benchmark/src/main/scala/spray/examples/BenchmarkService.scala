package spray.examples

import akka.actor.Actor
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import spray.can.Http
import spray.http.HttpMethods._
import spray.http.StatusCodes._
import spray.http.Uri.Path._
import spray.http.Uri._
import spray.http._
import spray.json._


/**
 * BenchmarkService
 * @author debop created at 2014. 5. 29.
 */
class BenchmarkService extends Actor {

    def jsonResponseEntity = HttpEntity(contentType = ContentTypes.`application/json`,
                                           string = JsObject("message" -> JsString("Hello World!")).compactPrint)

    def fastPath: Http.FastPath = {
        case HttpRequest(GET, Uri(_, _, Slash(Segment("fast-ping", Path.Empty)), _, _), _, _, _) =>
            HttpResponse(entity = "FAST-PONG!")

        case HttpRequest(GET, Uri(_, _, Slash(Segment("fast-json", Path.Empty)), _, _), _, _, _) =>
            HttpResponse(entity = jsonResponseEntity)
    }


    override def receive: Receive = {

        // 새로운 connection 요청이 왔을 때, 이 객체를 conneciton handler 로 등록합니다.
        case _: Http.Connected => sender ! Http.Register(self, fastPath = fastPath)

        case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
            sender ! HttpResponse(entity = HttpEntity(MediaTypes.`text/html`, indexHtml))

        case HttpRequest(GET, Uri.Path("/ping"), _, _, _) => sender ! HttpResponse(entity = "PONG!")

        case HttpRequest(GET, Uri.Path("/json"), _, _, _) => sender ! HttpResponse(entity = jsonResponseEntity)

        case HttpRequest(GET, Uri.Path("/stop"), _, _, _) =>
            sender ! HttpResponse(entity = "Shutting down in 1 second ...")
            context.system.scheduler.scheduleOnce(1.second) { context.system.shutdown() }

        case _: HttpRequest => sender ! HttpResponse(NotFound, entity = "Unknown resource!")

    }

    val indexHtml = <html>
                        <body>
                            <h1>Tiny
                                <i>spray-can</i>
                                benchmark server</h1>
                            <p>Defined resources:</p>
                            <ul>
                                <li>
                                    <a href="/ping">/ping</a>
                                </li>
                                <li>
                                    <a href="/fast-ping">/fast-ping</a>
                                </li>
                                <li>
                                    <a href="/json">/json</a>
                                </li>
                                <li>
                                    <a href="/fast-json">/fast-json</a>
                                </li>
                                <li>
                                    <a href="/stop">/stop</a>
                                </li>
                            </ul>
                        </body>
                    </html>.toString()
}
