package ru.nir.testproject.services.tools.config

case class AppConfig(
    app: AppSettings,
    postgres: DatabaseConfig,
    //kafka: KafkaConfig,
    api: ApiEndpoint,
    modeParams: ModeConfig,
    service: ServiceConfig
)
