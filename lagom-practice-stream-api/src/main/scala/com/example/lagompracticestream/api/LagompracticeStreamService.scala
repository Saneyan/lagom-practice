package com.example.lagompracticestream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
  * The lagom-practice stream interface.
  *
  * This describes everything that Lagom needs to know about how to serve and
  * consume the LagompracticeStream service.
  */
trait LagompracticeStreamService extends Service {

  def stream: ServiceCall[Source[String, NotUsed], Source[String, NotUsed]]

  override final def descriptor = {
    import Service._

    named("lagom-practice-stream")
      .withCalls(
        namedCall("stream", stream)
      ).withAutoAcl(true)
  }
}

