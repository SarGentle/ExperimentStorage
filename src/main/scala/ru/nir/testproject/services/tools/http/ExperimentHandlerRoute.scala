package ru.nir.testproject.services.tools.http

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import ru.nir.testproject.AppTask
import zio.interop.catz._

object ExperimentHandlerRoute extends Http4sDsl[AppTask] {

  val routes = HttpRoutes.of[AppTask] {
    case r @ POST -> Root / "setExperiment" =>
      for {
        resp <- Ok("OK")
      } yield resp

    case r @ GET -> Root / "getExperiment" =>
      for {
        resp <- Ok("OK")
      } yield resp
  }
}
