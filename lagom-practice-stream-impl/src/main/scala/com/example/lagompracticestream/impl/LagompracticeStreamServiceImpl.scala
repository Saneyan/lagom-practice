package com.example.lagompracticestream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.example.lagompracticestream.api.LagompracticeStreamService
import com.example.lagompractice.api.LagompracticeService

import scala.concurrent.Future

/**
  * Implementation of the LagompracticeStreamService.
  */
class LagompracticeStreamServiceImpl(lagompracticeService: LagompracticeService) extends LagompracticeStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(lagompracticeService.hello(_).invoke()))
  }
}
