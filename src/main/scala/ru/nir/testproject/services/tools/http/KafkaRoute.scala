package ru.nir.testproject.services.tools.http

import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import ru.nir.testproject.AppTask
import ru.nir.testproject.services.tools.kafka.KafkaService
import ru.nir.testproject.services.tools.config
import zio.ZIO
import zio.interop.catz.*

object KafkaRoute extends Http4sDsl[AppTask]{
  val routes = HttpRoutes.of[AppTask]{
    case r @ POST -> Root =>
      for{
        kafkaService <- ZIO.service[KafkaService]
        conf <- config.getKafkaConfig
        _ <- kafkaService.sendOneMessage("nir", "main", "11")
        resp <- Ok("Ok")
      }yield resp
  }
}
