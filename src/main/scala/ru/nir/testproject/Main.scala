package ru.nir.testproject

import ru.nir.testproject.services.tools.{config, http, kafka}
import ru.nir.testproject.services.logic.storageservices.experiments.ExperimentServiceLive
import ru.nir.testproject.services.logic.storageservices.files.FileServiceLive
import ru.nir.testproject.services.logic.storageservices.user.UserServiceLive
import ru.nir.testproject.services.tools.database.ExperimentDatabaseLive
import ru.nir.testproject.services.tools.kafka.KafkaServiceLive
import zio.{ExitCode, Scope, ZIO, ZIOAppArgs}

object Main extends zio.ZIOAppDefault {

  def program: ZIO[AppEnv, Throwable, ExitCode] = {
    for {
      config <- config.getAppConfig
      routes = http.httpApp()
      _ <- kafka.init().fork
      _ <- http.runHttp(routes, config.api)
    } yield ExitCode.success

  }

  override def run: ZIO[Main.Environment with ZIOAppArgs with Scope, Any, Any] =
    program
      .provide(
        zio.Scope.default,
        KafkaServiceLive.layer,
        ExperimentDatabaseLive.layer,
        ExperimentServiceLive.layer,
        FileServiceLive.layer,
        UserServiceLive.layer
      )
      .orDie
}
