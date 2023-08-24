package ru.nir.testproject.services.logic.storageservices.experiments

import ru.nir.testproject.model.database.Experiment
import ru.nir.testproject.services.tools.database.ExperimentDatabase
import zio.{RIO, Task, ZIO, ZLayer}

trait ExperimentService {
  def setExperiment(newExperiment: Experiment): Task[Unit]
  def getExperimentByName(experimentName: String): Task[Experiment]
}

object ExperimentService {
  def setExperiment(newExperiment: Experiment): RIO[ExperimentService, Unit] = {
    ZIO.environmentWithZIO[ExperimentService](_.get.setExperiment(newExperiment))
  }

  def getExperimentByName(experimentName: String): RIO[ExperimentService, Experiment] = {
    ZIO.environmentWithZIO[ExperimentService](_.get.getExperimentByName(experimentName))
  }
}

object ExperimentServiceLive {
  def layer =
    ZLayer {
      for {
        database <- ZIO.service[ExperimentDatabase]
      } yield new ExperimentServiceImpl(database)
    }
}
