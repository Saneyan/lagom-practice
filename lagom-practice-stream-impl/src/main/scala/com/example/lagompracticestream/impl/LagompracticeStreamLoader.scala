package com.example.lagompracticestream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.example.lagompracticestream.api.LagompracticeStreamService
import com.example.lagompractice.api.LagompracticeService
import com.softwaremill.macwire._

class LagompracticeStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagompracticeStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagompracticeStreamApplication(context) with LagomDevModeComponents

  override def describeServices = List(
    readDescriptor[LagompracticeStreamService]
  )
}

abstract class LagompracticeStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the services that this server provides
  override lazy val lagomServer = LagomServer.forServices(
    bindService[LagompracticeStreamService].to(wire[LagompracticeStreamServiceImpl])
  )

  // Bind the LagompracticeService client
  lazy val lagompracticeService = serviceClient.implement[LagompracticeService]
}
