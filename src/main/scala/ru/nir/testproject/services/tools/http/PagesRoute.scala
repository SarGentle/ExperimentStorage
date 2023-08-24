package ru.nir.testproject.services.tools.http

import org.http4s.{HttpRoutes, StaticFile}
import org.http4s.dsl.Http4sDsl
import ru.nir.testproject.AppTask
import zio.interop.catz._

object PagesRoute extends Http4sDsl[AppTask] {
  val routes = HttpRoutes.of[AppTask] {
    case r @ GET -> Root / "index" =>
      StaticFile.fromResource("/static/index.html", Some(r)).getOrElseF(NotFound())

    case r @ GET -> Root / "main" =>
      StaticFile.fromResource("/static/main.html", Some(r)).getOrElseF(NotFound())

    case r @ GET -> Root / "upload" =>
      StaticFile.fromResource("/static/upload.html", Some(r)).getOrElseF(NotFound())

    case r @ GET -> Root / "download" =>
      StaticFile.fromResource("/static/download.html", Some(r)).getOrElseF(NotFound())
  }
}
