package com.example.lagompractice.impl

import java.sql.Connection

import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, ReadSideProcessor}
import com.lightbend.lagom.scaladsl.persistence.jdbc.{JdbcReadSide, JdbcSession}

import scala.concurrent.ExecutionContext

class MessageRepository(session: JdbcSession)(implicit ec: ExecutionContext) {

}

class MessageEventProcessor(readSide: JdbcReadSide)(implicit ec: ExecutionContext) extends ReadSideProcessor[MessageEvent] {

  import JdbcSession.tryWith

  override def aggregateTags = MessageEvent.Tag.allTags

  override def buildHandler() = {
    readSide.builder[MessageEvent]("messageEventOffset")
      .setGlobalPrepare(createTables)
      .setEventHandler[MessageCreated](processMessageChanged)
      .build()
  }

  private def createTables(connection: Connection): Unit = {
    tryWith(connection.prepareStatement("""
      CREATE TABLE IF NOT EXISTS messages (
        messageId varchar(36),
        message TEXT,
        PRIMARY KEY (messageId)
      )
    """))(_.execute())
  }

  private def processMessageChanged(connection: Connection, eventElement: EventStreamElement[MessageCreated]): Unit = {
    tryWith(connection.prepareStatement("INSERT INTO messages (messageId, message) VALUES (?, ?)")) { ps =>
      ps.setString(1, eventElement.event.message.id.toString)
      ps.setString(2, eventElement.event.message.message)
      ps.execute()
    }
  }

  /*
  private def createTables(connection: Connection): Unit = {
    tryWith(connection.prepareStatement("INSERT INTO messages (message) VALUES (?)")) { ps =>
      ps.execute()
    }
  }
  */
}