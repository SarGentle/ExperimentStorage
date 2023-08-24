package ru.nir.testproject.services.tools.config

final case class AppSettings(
    appName: String,
    debugMode: Boolean = false
)
