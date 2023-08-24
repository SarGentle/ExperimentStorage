package ru.nir.testproject.services.tools.config

final case class DatabaseConfig(
    driverClassName: String,
    url: String,
    username: String,
    password: String,
    maxPoolSize: Int,
    connectionTimeoutMs: Long,
    socketTimeoutS: Int,
    leakDetectionThresholdMs: Long,
    threadPoolSize: Int,
    dbScheme: Option[String] = None
)
