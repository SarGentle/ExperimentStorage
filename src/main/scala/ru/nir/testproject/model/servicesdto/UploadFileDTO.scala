package ru.nir.testproject.model.servicesdto

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class UploadFileDTOReq(
    fileName: String,
    fileType: String,
    parsed: Int,
    experiment: String
)

object UploadFileDTOReq {
  implicit val encoder: Encoder[UploadFileDTOReq] = deriveEncoder[UploadFileDTOReq]
  implicit val decoder: Decoder[UploadFileDTOReq] = deriveDecoder[UploadFileDTOReq]
}
