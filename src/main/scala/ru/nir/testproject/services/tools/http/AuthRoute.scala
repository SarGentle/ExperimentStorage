package ru.nir.testproject.services.tools.http

import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import ru.nir.testproject.AppTask
import ru.nir.testproject.model.servicesdto.{AuthUserReq, AuthUserRes}
import ru.nir.testproject.services.tools.database.ExperimentDatabase
import zio.ZIO
import zio.interop.catz._

object AuthRoute extends Http4sDsl[AppTask] {
  val routes = HttpRoutes.of[AppTask] {
    case r @ POST -> Root / "post" =>
      for {
        database   <- ZIO.service[ExperimentDatabase]
        json       <- r.as[AuthUserReq]
        userOption <- database.getUserByLoginAndPassword(json.login, json.password)
        resp       <- if (userOption.isDefined) Ok(AuthUserRes(1)) else BadRequest(AuthUserRes(0))
      } yield resp

    case r @ GET -> Root / "get" =>
      for {
        resp <- Ok("OK")
      } yield resp
  }
}
