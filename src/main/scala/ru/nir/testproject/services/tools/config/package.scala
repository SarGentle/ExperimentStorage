package ru.nir.testproject.services.tools

import zio.config.*
import zio.config.magnolia.*
import typesafe.TypesafeConfigProvider

package object config {

  private val appConfig = deriveConfig[AppConfig]

  val getAppConfig = read(
    appConfig from TypesafeConfigProvider.fromTypesafeConfig(com.typesafe.config.ConfigFactory.load())
  )

  val getModeParams     = getAppConfig.map(_.modeParams)
  val getPostgresConfig = getAppConfig.map(_.postgres)
  val getKafkaConfig    = getAppConfig.map(_.kafka)
  val getApiEndpoint    = getAppConfig.map(_.api)
}
