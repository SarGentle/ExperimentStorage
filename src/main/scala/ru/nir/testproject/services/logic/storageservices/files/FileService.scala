package ru.nir.testproject.services.logic.storageservices.files

import org.http4s.multipart.Part
import ru.nir.testproject.{AppEnv, AppTask}
import ru.nir.testproject.services.tools.database.ExperimentDatabase
import zio.{RIO, Scope, Task, ZIO, ZLayer}

trait FileService {
  def uploadFile(parts: Vector[Part[AppTask]]): RIO[AppEnv, Unit]
  def downloadFile(fileName: String, experimentName: String, fileType: String): RIO[Scope, Array[Byte]]
}

object FileService {
  def uploadFile(parts: Vector[Part[AppTask]]): RIO[AppEnv, Unit] = {
    ZIO.environmentWithZIO[FileService](_.get.uploadFile(parts))
  }
  def downloadFile(
      fileName: String,
      experimentName: String,
      fileType: String
  ): RIO[FileService with Scope, Array[Byte]] = {
    ZIO.environmentWithZIO[FileService](_.get.downloadFile(fileName, experimentName, fileType))
  }
}

object FileServiceLive {
  def layer =
    ZLayer {
      for {
        database <- ZIO.service[ExperimentDatabase]
      } yield new FileServiceImpl(database)
    }
}
