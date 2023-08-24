package ru.nir.testproject.services.logic.storageservices.experiments
import ru.nir.testproject.model.database.Experiment
import ru.nir.testproject.services.tools.database.ExperimentDatabase
import zio.{Task, ZIO}

import java.io.File
import java.nio.file.{Files, Paths}

class ExperimentServiceImpl(database: ExperimentDatabase) extends ExperimentService {

  override def setExperiment(newExperiment: Experiment): Task[Unit] = {
    for {
      experimentOption <- database.selectExperimentByName(newExperiment.experimentName)
      _ <- ZIO.when(experimentOption.isEmpty) {
        for {
          _                      <- database.insertExperiment(newExperiment)
          experimentFromDBOption <- database.selectExperimentByName(newExperiment.experimentName)
          experimentFromDB <-
            ZIO.fromOption(experimentFromDBOption).mapError(_ => new Exception("Experiment not found in DB"))
          _ <- ZIO.attempt(
            Files.createDirectory(
              Paths.get("D:" + File.separator + "ExpStorage" + File.separator + experimentFromDB.experimentID.toString)
            )
          )
        } yield ()
      }
    } yield ()
  }

  override def getExperimentByName(experimentName: String): Task[Experiment] = {
    for {
      experimentOption <- database.selectExperimentByName(experimentName)
      experiment       <- ZIO.fromOption(experimentOption).mapError(_ => new Exception("Experiment not found in DB"))
    } yield experiment
  }

}
