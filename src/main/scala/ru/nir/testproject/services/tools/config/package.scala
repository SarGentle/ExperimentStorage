package ru.nir.testproject.services.tools

import pureconfig.ConfigSource
import pureconfig.generic.auto._
// import zio.kafka.admin.AdminClientSettings
import zio.{Layer, URIO, ZIO, ZLayer}

package object config {

  val getAppConfig: URIO[AppConfig, AppConfig] = ZIO.service

  val getModeParams: URIO[ModeConfig, ModeConfig] = ZIO.service

  val getPostgresConfig: URIO[AppConfig, DatabaseConfig] = getAppConfig.map(_.postgres)

  val postgresConfigLive: ZLayer[AppConfig, Throwable, DatabaseConfig] = ZLayer(getAppConfig.map(_.postgres))

  val modeParams: ZLayer[AppConfig, Throwable, ModeConfig] = ZLayer(getAppConfig.map(_.modeParams))

  val serviceConfig: ZLayer[AppConfig, Throwable, ServiceConfig] = ZLayer(getAppConfig.map(_.service))

  val live: Layer[Throwable, AppConfig] = {
    ZLayer(
      ZIO
        .fromEither(ConfigSource.default.load[AppConfig])
        .mapError(failures =>
          new IllegalStateException(
            s"Error loading configuration: $failures"
          )
        )
    )
  }

}
