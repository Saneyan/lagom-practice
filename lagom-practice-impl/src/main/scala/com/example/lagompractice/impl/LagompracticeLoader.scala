package com.example.lagompractice.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.example.lagompractice.api.LagompracticeService
import com.lightbend.lagom.scaladsl.persistence.jdbc.JdbcPersistenceComponents
import com.softwaremill.macwire._
import play.api.db.HikariCPComponents

class LagompracticeLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagompracticeApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagompracticeApplication(context) with LagomDevModeComponents

  override def describeServices = List(
    readDescriptor[LagompracticeService]
  )
}

abstract class LagompracticeApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with JdbcPersistenceComponents
    with HikariCPComponents
    with AhcWSComponents {

  readSide.register(wire[MessageEventProcessor])

  // Bind the services that this server provides
  override lazy val lagomServer = LagomServer.forServices(
    bindService[LagompracticeService].to(wire[LagompracticeServiceImpl])
  )

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = LagompracticeSerializerRegistry

  // Register the lagom-practice persistent entity
  persistentEntityRegistry.register(wire[LagompracticeEntity])
  persistentEntityRegistry.register(wire[MessageEntity])
}
