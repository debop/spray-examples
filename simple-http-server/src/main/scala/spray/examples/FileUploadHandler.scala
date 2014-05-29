package spray.examples

import akka.actor.{Actor, ActorLogging, ActorRef}
import spray.http._
import akka.actor.Actor.Receive
import spray.io.CommandWrapper
import scala.concurrent.duration.Duration
import java.io.{FileOutputStream, File}

/**
 * FileUploadHandler
 * @author debop created at 2014. 5. 29.
 */
class FileUploadHandler(client:ActorRef, start:ChunkedRequestStart) extends Actor with ActorLogging {
    import start.request._

    client ! CommandWrapper(SetRequestTimeout(Duration.Inf))     // cancel timeout

    val tmpFile = File.createTempFile("chunked-receiver", ".tmp", new File("/tmp"))
    tmpFile.deleteOnExit()
    val output = new FileOutputStream(tmpFile)

    override def receive: Receive = {

    }
}


