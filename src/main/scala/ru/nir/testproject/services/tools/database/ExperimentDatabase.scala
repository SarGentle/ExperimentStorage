package ru.nir.testproject.services.tools.database

import ru.nir.testproject.model.database.{Experiment, FileInstance, UserDatabase}
import ru.nir.testproject.model.servicesdto.RegisterNewUserReq
import ru.nir.testproject.services.tools.config
import ru.nir.testproject.services.tools.database
import zio.{RIO, Task, ZIO, ZLayer}

trait ExperimentDatabase {
  def insertOneRawFileByExperiment(file: FileInstance): Task[Int]
  def insertOneParsedFileByExperiment(file: FileInstance): Task[Int]
  def selectOneFileByExperiment(experimentName: Int, fileName: String): Task[Option[FileInstance]]
  def selectAllFilesOfExperiment(experimentName: Int): Task[List[FileInstance]]

  def insertExperiment(experiment: Experiment): Task[Int]
  def selectExperimentByName(experimentName: String): Task[Option[Experiment]]

  def getUserByLoginAndPassword(login: String, password: String): Task[Option[UserDatabase]]
  def setNewUser(newUser: RegisterNewUserReq): Task[Int]
}

object ExperimentDatabase {
  def insertOneRawFileByExperiment(file: FileInstance): RIO[ExperimentDatabase, Int] = {
    ZIO.environmentWithZIO[ExperimentDatabase](_.get.insertOneRawFileByExperiment(file))
  }
  def insertOneParsedFileByExperiment(file: FileInstance): RIO[ExperimentDatabase, Int] = {
    ZIO.environmentWithZIO[ExperimentDatabase](_.get.insertOneParsedFileByExperiment(file))
  }
  def selectOneFileByExperiment(experimentId: Int, fileName: String): RIO[ExperimentDatabase, Option[FileInstance]] = {
    ZIO.environmentWithZIO[ExperimentDatabase](_.get.selectOneFileByExperiment(experimentId, fileName))
  }
  def selectAllFilesOfExperiment(experimentId: Int): RIO[ExperimentDatabase, List[FileInstance]] = {
    ZIO.environmentWithZIO[ExperimentDatabase](_.get.selectAllFilesOfExperiment(experimentId))
  }

  def insertExperiment(experiment: Experiment): RIO[ExperimentDatabase, Int] = {
    ZIO.environmentWithZIO[ExperimentDatabase](_.get.insertExperiment(experiment))
  }
  def selectExperimentByName(experimentName: String): RIO[ExperimentDatabase, Option[Experiment]] = {
    ZIO.environmentWithZIO[ExperimentDatabase](_.get.selectExperimentByName(experimentName))
  }
  
  def getUserByLoginAndPassword(login: String, password: String): RIO[ExperimentDatabase, Option[UserDatabase]] = {
    ZIO.environmentWithZIO[ExperimentDatabase](_.get.getUserByLoginAndPassword(login, password))
  }
  def setNewUser(newUser: RegisterNewUserReq): RIO[ExperimentDatabase, Int] =
    ZIO.environmentWithZIO[ExperimentDatabase](_.get.setNewUser(newUser))
}

object ExperimentDatabaseLive {
  def layer =
    ZLayer {
      for {
        config        <- config.getAppConfig
        postgrTrnsctr <- database.managedTransactor(config.postgres, config.app.appName)
      } yield new ExperimentDatabaseImpl(postgrTrnsctr)
    }
}
