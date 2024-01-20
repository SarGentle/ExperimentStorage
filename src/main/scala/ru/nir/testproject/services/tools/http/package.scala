package ru.nir.testproject.services.tools

import org.http4s.blaze.server.BlazeServerBuilder

import org.http4s.implicits._
import org.http4s.HttpApp
import org.http4s.server.Router
import org.http4s.server.middleware.{CORS, CORSPolicy}
import ru.nir.testproject.AppTask

import scala.concurrent.duration.DurationInt
import ru.nir.testproject.services.tools.config.ApiEndpoint
import zio.interop.catz._

package object http {

  val cors: CORSPolicy = CORS.policy.withAllowOriginAll
    .withAllowCredentials(false)

  def runHttp(
      httpApp: HttpApp[AppTask],
      apiConfig: ApiEndpoint
  ) = {

    BlazeServerBuilder[AppTask]
      .withIdleTimeout(apiConfig.timeoutSec seconds)
      .withResponseHeaderTimeout((apiConfig.timeoutSec - 10) seconds)
      .bindHttp(apiConfig.port, apiConfig.host)
      .withHttpApp(cors(httpApp))
      .serve
      .compile
      .drain
  }

  def httpApp() = {

    Router(
      "/file"       -> FileHandlerRoute.routes,
      "/experiment" -> ExperimentHandlerRoute.routes,
      "/auth"       -> AuthRoute.routes,
      "kafka"       -> KafkaRoute.routes,
      ""            -> PagesRoute.routes
    ).orNotFound
  }

}
