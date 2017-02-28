package com.example.lagompractice.impl

import java.util.UUID

import akka.Done
import com.example.lagompractice.api.Message
import com.example.lagompractice.utils.JsonFormats._
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json._

class MessageEntity extends PersistentEntity {

  override type Command = MessageCommand
  override type Event = MessageEvent
  override type State = Option[Message]

  override def initialState: Option[Message] = None

  override def behavior: Behavior = {
    case None => Actions().onCommand[CreateMessage, Done] {

      case (CreateMessage(newMessage), ctx, state) =>
        ctx.thenPersist(
          MessageCreated(newMessage)
        ) { _ =>
          ctx.reply(Done)
        }

    }.onReadOnlyCommand[GetMessage.type, Option[Message]] {

      case (GetMessage, ctx, state) =>
        ctx.reply(state)

    }.onEvent {

      case (MessageCreated(newMessage), state) => Some(newMessage)
    }

    case Some(message) => Actions().onReadOnlyCommand[GetMessage.type, Option[Message]] {

      case (GetMessage, ctx, state) =>
        ctx.reply(state)

    }
  }
}

case class Message(id: UUID, message: String)

object Message {

  implicit val format: Format[Message] = Json.format[Message]
}

trait MessageCommand

object GetMessage extends MessageCommand with ReplyType[Option[Message]] {

  implicit val format: Format[GetMessage.type] = singletonFormat(GetMessage)
}

object CreateMessage {

  implicit val format: Format[CreateMessage] = Json.format
}

case class CreateMessage(message: Message) extends MessageCommand with ReplyType[Done]

object MessageEvent {

  val NumShards = 4
  val Tag = AggregateEventTag.sharded[MessageEvent](NumShards)
}

sealed trait MessageEvent extends AggregateEvent[MessageEvent] {

  override def aggregateTag = MessageEvent.Tag
}

object MessageCreated {

  implicit val format: Format[MessageCreated] = Json.format
}

case class MessageCreated(message: Message) extends MessageEvent
