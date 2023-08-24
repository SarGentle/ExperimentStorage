package ru.nir.testproject.services.tools.database

import doobie._
import doobie.implicits._
import doobie.Transactor
import doobie.postgres.implicits._
import doobie.postgres._
import doobie.implicits.toSqlInterpolator
import ru.nir.testproject.model.database.{Experiment, FileInstance, UserDatabase}
import zio.Task
import zio.interop.catz._

class ExperimentDatabaseImpl(postgreeTransactor: Transactor[Task]) extends ExperimentDatabase {

  override def selectAllFilesOfExperiment(experimentId: Int): Task[List[FileInstance]] = {
    sql"""SELECT * FROM experimentstorage."NIR"."File" WHERE "Experiment" = $experimentId"""
      .query[FileInstance]
      .to[List]
      .transact(postgreeTransactor)
  }

  override def selectOneFileByExperiment(experimentId: Int, fileName: String): Task[Option[FileInstance]] = {
    sql"""SELECT * FROM experimentstorage."NIR"."File" WHERE "Experiment" = $experimentId AND "FileName" = $fileName"""
      .query[FileInstance]
      .option
      .transact(postgreeTransactor)
  }

  override def insertOneParsedFileByExperiment(file: FileInstance): Task[Int] = {
    sql"""INSERT INTO experimentstorage."NIR"."File" ("FileName", "FileType", "FilePath", "Experiment", "FileParsed")
         VALUES(
                ${file.fileName},
                ${file.fileType},
                ${file.filePath},
                ${file.experiment},
                ${file.fileParsed}
         )
       """.update.run.transact[Task](postgreeTransactor)
  }

  override def insertOneRawFileByExperiment(file: FileInstance): Task[Int] = {
    sql"""INSERT INTO experimentstorage."NIR"."File" ("FileName", "FileType", "FilePath", "Experiment", "FileParsed")
         VALUES(
                ${file.fileName},
                ${file.fileType},
                ${file.filePath},
                ${file.experiment},
                ${file.fileParsed}
         )
       """.update.run.transact[Task](postgreeTransactor)
  }

  override def insertExperiment(experiment: Experiment): Task[Int] = {
    sql"""INSERT INTO experimentstorage."NIR"."Experiment" ("ExperimentName", "ExperimentDesc", "ExperimentOrg", "ExperimentAuthor", "ExperimentDate")
         VALUES (
                 ${experiment.experimentName},
                 ${experiment.experimentDesc},
                 ${experiment.experimentOrg},
                 ${experiment.experimentAuthor},
                 ${experiment.experimentDate}
         )""".update.run.transact[Task](postgreeTransactor)
  }

  override def selectExperimentByName(experimentName: String): Task[Option[Experiment]] = {
    sql"""SELECT * FROM experimentstorage."NIR"."Experiment" WHERE "ExperimentName" = $experimentName"""
      .query[Experiment]
      .option
      .transact(postgreeTransactor)
  }

  override def getUserByLoginAndPassword(login: String, password: String): Task[Option[UserDatabase]] = {
    sql"""SELECT * FROM experimentstorage."NIR"."User" WHERE "UserLogin" = $login AND "Password" = $password"""
      .query[UserDatabase]
      .option
      .transact(postgreeTransactor)
  }
}
