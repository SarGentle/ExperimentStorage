package ru.nir.testproject.model.servicesdto

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class DownloadFileDTOReq(
    experimentName: String,
    fileName: String,
    fileType: String
)

object DownloadFileDTOReq {
  implicit val encoder: Encoder[DownloadFileDTOReq] = deriveEncoder[DownloadFileDTOReq]
  implicit val decoder: Decoder[DownloadFileDTOReq] = deriveDecoder[DownloadFileDTOReq]
}
