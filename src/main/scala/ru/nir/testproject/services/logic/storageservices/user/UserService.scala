package ru.nir.testproject.services.logic.storageservices.user

import ru.nir.testproject.{AppTask, AppEnv}
import org.http4s.multipart.Part
import zio.interop.catz._
import zio.{Task, RIO, ZIO, ZLayer}

trait UserService {
  def setNewUser(multipart: Vector[Part[AppTask]]): RIO[AppEnv, Unit]

  def setNewAvatar(login: String, picture: Part[AppTask]): RIO[AppEnv, Unit]
}

object UserService{
  def setNewUser(multipart: Vector[Part[AppTask]]): RIO[AppEnv, Unit] =
    ZIO.environmentWithZIO[UserService](_.get.setNewUser(multipart))

  def setNewAvatar(login: String, picture: Part[AppTask]): RIO[AppEnv, Unit] =
    ZIO.environmentWithZIO[UserService](_.get.setNewAvatar(login, picture))
}

object UserServiceLive{
  def layer = ZLayer.succeed(new UserServiceImpl)
}