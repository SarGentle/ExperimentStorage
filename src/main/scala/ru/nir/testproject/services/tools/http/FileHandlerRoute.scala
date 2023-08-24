package ru.nir.testproject.services.tools.http

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.multipart.Multipart
import ru.nir.testproject.AppTask
import ru.nir.testproject.services.logic.storageservices.files.FileService
import zio.ZIO
import zio.interop.catz._

object FileHandlerRoute extends Http4sDsl[AppTask] {
  val routes = HttpRoutes.of[AppTask] {
    case r @ GET -> Root / "getFile" =>
      for {
        resp <- Ok("OK")
      } yield resp

    case req @ POST -> Root / "setFile" =>
      val re = for {
        fileService <- ZIO.service[FileService]
        resp <- req.decode[Multipart[AppTask]] { m =>
          for {
            parts  <- ZIO.attempt(m.parts)
            _      <- fileService.uploadFile(parts)
            respJs <- Ok("OK")
          } yield respJs
        }
      } yield resp
      re.tapError(err => zio.Console.printLine(s"------------------------------${err.getMessage}"))
  }
}
