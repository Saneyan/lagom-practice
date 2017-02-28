package com.example.lagompractice.impl

import java.util.UUID

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.example.lagompractice.api.{LagompracticeService, Message => ApiMessage}
import com.fasterxml.uuid.Generators
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import scala.concurrent.ExecutionContext

/**
  * Implementation of the LagompracticeService.
  */
class LagompracticeServiceImpl(persistentEntityRegistry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends LagompracticeService {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the lagom-practice entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagompracticeEntity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id, None))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the lagom-practice entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagompracticeEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }

  override def getMessage(id: UUID) = ServiceCall { _ =>
    val ref = persistentEntityRegistry.refFor[MessageEntity](id.toString)
    ref.ask(GetMessage).map {
      case Some(message) => ApiMessage(Some(message.id), message.message)
      case None => throw NotFound(s"A message '$id' is not found.")
    }
  }

  override def postMessage = ServiceCall { message =>
    val uuid = Generators.timeBasedGenerator().generate()
    val newMessage = Message(uuid, message.message)
    val ref = persistentEntityRegistry.refFor[MessageEntity](uuid.toString)
    ref.ask(CreateMessage(newMessage)).map { _ =>
      ApiMessage(Some(newMessage.id), newMessage.message)
    }
  }
}
