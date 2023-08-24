package ru.nir.testproject.model.servicesdto

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class AuthUserReq(
    login: String,
    password: String
)

case class AuthUserRes(
    result: Int
)

object AuthUserReq {
  implicit val decoder: Decoder[AuthUserReq] = deriveDecoder[AuthUserReq]
  implicit val encoder: Encoder[AuthUserReq] = deriveEncoder[AuthUserReq]
}

object AuthUserRes {
  implicit val decoder: Decoder[AuthUserRes] = deriveDecoder[AuthUserRes]
  implicit val encoder: Encoder[AuthUserRes] = deriveEncoder[AuthUserRes]
}
