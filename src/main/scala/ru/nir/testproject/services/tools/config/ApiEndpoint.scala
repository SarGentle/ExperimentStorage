package ru.nir.testproject.services.tools.config

import scala.concurrent.duration.Duration

case class ApiEndpoint(host: String, port: Int, timeout: Duration)
