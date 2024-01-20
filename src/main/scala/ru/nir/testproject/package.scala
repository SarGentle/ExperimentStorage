package ru.nir

import ru.nir.testproject.services.logic.storageservices.experiments.ExperimentService
import ru.nir.testproject.services.logic.storageservices.files.FileService
import ru.nir.testproject.services.logic.storageservices.user.UserService
import ru.nir.testproject.services.tools.database.ExperimentDatabase
import ru.nir.testproject.services.tools.kafka.KafkaService
import zio.{RIO, Scope}

package object testproject {

  type AppEnv =
    Scope with ExperimentService with FileService with ExperimentDatabase with KafkaService with UserService

  type AppTask[A] = RIO[AppEnv, A]
}
