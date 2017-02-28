package com.example.lagompractice.api

import java.util.UUID

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

/**
  * The lagom-practice service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the LagompracticeService.
  */
trait LagompracticeService extends Service {

  /**
    * Example: curl http://localhost:9000/api/hello/Alice
    */
  def hello(id: String): ServiceCall[NotUsed, String]

  /**
    * Example: curl -H "Content-Type: application/json" -X POST -d '{"message":
    * "Hi"}' http://localhost:9000/api/hello/Alice
    */
  def useGreeting(id: String): ServiceCall[GreetingMessage, Done]

  def getMessage(id: UUID): ServiceCall[NotUsed, Message]

  def postMessage: ServiceCall[Message, Message]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("lagom-practice").withCalls(
      pathCall("/api/hello/:id", hello _),
      pathCall("/api/hello/:id", useGreeting _),
      pathCall("/api/messages/:id", getMessage _),
      pathCall("/api/messages", postMessage _)
    ).withAutoAcl(true)
    // @formatter:on
  }
}

case class Message(id: Option[UUID], message: String)

object Message {

  implicit val format: Format[Message] = Json.format[Message]
}

/**
  * The greeting message class.
  */
case class GreetingMessage(message: String)

object GreetingMessage {
  /**
    * Format for converting greeting messages to and from JSON.
    *
    * This will be picked up by a Lagom implicit conversion from Play's JSON format to Lagom's message serializer.
    */
  implicit val format: Format[GreetingMessage] = Json.format[GreetingMessage]
}
