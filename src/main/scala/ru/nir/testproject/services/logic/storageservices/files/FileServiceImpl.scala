package ru.nir.testproject.services.logic.storageservices.files

import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.multipart.Part
import ru.nir.testproject.{AppEnv, AppTask}
import ru.nir.testproject.model.servicesdto.UploadFileDTOReq
import ru.nir.testproject.services.tools.database.ExperimentDatabase
import zio.{RIO, Scope, ZIO}
import zio.interop.catz._

import java.io.{File, FileInputStream}
import java.nio.file.Paths
import ru.nir.testproject.model.database.FileInstance

class FileServiceImpl(database: ExperimentDatabase) extends FileService {

  override def uploadFile(parts: Vector[Part[AppTask]]): RIO[AppEnv, Unit] = {
    for {
      jsonInfoPart <-
        ZIO
          .fromOption(parts.find(_.name.contains("request")))
          .mapError(_ => new Exception("Not found part with name=request by uploading file"))
      jsonInfo         <- jsonInfoPart.as[UploadFileDTOReq]
      fileName         <- ZIO.attempt(jsonInfo.fileName)
      experimentName   <- ZIO.attempt(jsonInfo.experiment)
      experimentOption <- database.selectExperimentByName(experimentName)
      experiment       <- ZIO.fromOption(experimentOption).mapError(_ => new Exception("Experiment not found in DB"))
      experimentFolder <-
        ZIO.attempt(Paths.get("C:" + File.separator + "ExpStorage" + File.separator + experiment.experimentID.toString))
      filePath <- ZIO.attempt(experimentFolder.resolve(Paths.get(fileName)))
      file <-
        ZIO
          .fromOption(parts.find(_.filename.contains(fileName)))
          .mapError(_ => new Exception(s"Not found document part with filename = $fileName"))
      _ <- file.body.through(fs2.io.file.writeAll[AppTask](filePath)).compile.drain
      _ <- database.insertOneRawFileByExperiment(
        FileInstance(
          222,
          fileName,
          jsonInfo.fileType,
          experimentFolder.toString,
          experiment.experimentID,
          jsonInfo.parsed
        )
      )
    } yield ()
  }

  override def downloadFile(fileName: String, experimentName: String, fileType: String): RIO[Scope, Array[Byte]] = {
    for {
      experimentOption <- database.selectExperimentByName(experimentName)
      experiment       <- ZIO.fromOption(experimentOption).mapError(_ => new Exception("Experiment not found in DB"))
      fileOption       <- database.selectOneFileByExperiment(experiment.experimentID, fileName)
      file             <- ZIO.fromOption(fileOption).mapError(_ => new Exception("File not found in DB"))
      inputStr <- ZIO.scoped {
        ZIO
          .fromAutoCloseable(
            ZIO.attemptBlocking(
              new FileInputStream(file.filePath)
            )
          )
          .map(_.readAllBytes())
      }
    } yield inputStr
  }
}
