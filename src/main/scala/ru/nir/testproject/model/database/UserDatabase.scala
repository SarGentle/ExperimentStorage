package ru.nir.testproject.model.database

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class UserDatabase(
    id: Int,
    login: String,
    email: String,
    password: String
)

object UserDatabase {
  implicit val decoder: Decoder[UserDatabase] = deriveDecoder[UserDatabase]
  implicit val encoder: Encoder[UserDatabase] = deriveEncoder[UserDatabase]
}
