package ru.nir

import ru.nir.testproject.services.logic.storageservices.experiments.ExperimentService
import ru.nir.testproject.services.logic.storageservices.files.FileService
import ru.nir.testproject.services.tools.config.AppConfig
import ru.nir.testproject.services.tools.database.ExperimentDatabase
import zio.{RIO, Scope}

package object testproject {

  type AppEnv =
    Scope with ExperimentService with FileService with ExperimentDatabase with AppConfig

  type AppTask[A] = RIO[AppEnv, A]
}
