package ru.nir.testproject.services.tools.database

import doobie.*
import doobie.implicits.*
import doobie.Transactor
import doobie.postgres.implicits.*
import doobie.postgres.*
import doobie.implicits.toSqlInterpolator
import ru.nir.testproject.model.database.{Experiment, FileInstance, UserDatabase}
import ru.nir.testproject.model.servicesdto.RegisterNewUserReq
import zio.Task
import zio.interop.catz.*

class ExperimentDatabaseImpl(postgreeTransactor: Transactor[Task]) extends ExperimentDatabase {

  override def selectAllFilesOfExperiment(experimentId: Int): Task[List[FileInstance]] = {
    sql"""SELECT * FROM experimentstorage."NIR"."File" WHERE "Experiment" = $experimentId"""
      .query[FileInstance]
      .to[List]
      .transact(postgreeTransactor)
  }

  override def selectOneFileByExperiment(experimentId: Int, fileName: String): Task[Option[FileInstance]] = {
    sql"""SELECT * FROM experimentstorage."NIR"."File" WHERE "Experiment" = $experimentId AND "name" = $fileName"""
      .query[FileInstance]
      .option
      .transact(postgreeTransactor)
  }

  override def insertOneParsedFileByExperiment(file: FileInstance): Task[Int] = {
    sql"""INSERT INTO experimentstorage."NIR"."File" ("name", "type", "filePath", "Experiment", "fileParsed")
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
    sql"""INSERT INTO experimentstorage."NIR"."File" ("name", "type", "filePath", "Experiment", "fileParsed")
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
    sql"""INSERT INTO experimentstorage."NIR"."Experiment" ("name", "description", "organization", "author", "date")
         VALUES (
                 ${experiment.experimentName},
                 ${experiment.experimentDesc},
                 ${experiment.experimentOrg},
                 ${experiment.experimentAuthor},
                 ${experiment.experimentDate}
         )""".update.run.transact[Task](postgreeTransactor)
  }

  override def selectExperimentByName(experimentName: String): Task[Option[Experiment]] = {
    sql"""SELECT * FROM experimentstorage."NIR"."Experiment" WHERE "name" = $experimentName"""
      .query[Experiment]
      .option
      .transact(postgreeTransactor)
  }

  override def getUserByLoginAndPassword(login: String, password: String): Task[Option[UserDatabase]] = {
    sql"""SELECT * FROM experimentstorage."NIR"."User" WHERE "login" = $login AND "password" = $password"""
      .query[UserDatabase]
      .option
      .transact(postgreeTransactor)
  }

  override def setNewUser(newUser: RegisterNewUserReq) = {
    sql"""INSERT INTO experimentstorage."NIR"."User" ("login", "email", "password") VALUES (${newUser.login},${newUser.email},${newUser.password})"""
      .update
      .run
      .transact[Task](postgreeTransactor)
  }
}
