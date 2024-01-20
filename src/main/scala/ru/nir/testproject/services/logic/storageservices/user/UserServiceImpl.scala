package ru.nir.testproject.services.logic.storageservices.user
import org.http4s.multipart.Part
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import ru.nir.testproject.model.servicesdto.RegisterNewUserReq
import ru.nir.testproject.services.tools.database.ExperimentDatabase
import ru.nir.testproject.{AppEnv, AppTask}
import zio.{RIO, Task, ZIO}
import zio.interop.catz.*

import java.io.File
import java.nio.file.Paths

class UserServiceImpl extends UserService {

  override def setNewUser(multipart: Vector[Part[AppTask]]): RIO[AppEnv, Unit] = {
    for{
      database <- ZIO.service[ExperimentDatabase]
      jsonInfoPart <- ZIO
        .fromOption(multipart.find(_.name.contains("request")))
        .mapError(_ => new Exception("Not found part with name=request for new user"))
      jsonInfo <- jsonInfoPart.as[RegisterNewUserReq]
      picturePart <- ZIO
        .fromOption(multipart.find(_.name.contains(jsonInfo.login)))
        .mapError(_ => new Exception(s"Not found file's part with name=${jsonInfo.login} for set up avatar"))
      _ <- database.setNewUser(jsonInfo)
      _ <- setNewAvatar(jsonInfo.login, picturePart)
    }yield ()
  }

  override def setNewAvatar(login: String, picture: Part[AppTask]): RIO[AppEnv, Unit] = {
    for{
      filePath <- ZIO.attempt(Paths.get(s"D:/ScalaProjects/ExperimentStorage/src/main/resources/static/avatars/$login.jpg"))
      _ <- picture.body.through(fs2.io.file.writeAll[AppTask](filePath)).compile.drain
    }yield ()
  }
}
