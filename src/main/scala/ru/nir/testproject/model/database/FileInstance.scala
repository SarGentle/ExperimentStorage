package ru.nir.testproject.model.database

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class FileInstance(
    fileId: Int,
    fileName: String,
    fileType: String,
    filePath: String,
    experiment: Int,
    fileParsed: Int
)

object FileInstance {
  implicit val decoder: Decoder[FileInstance] = deriveDecoder[FileInstance]
  implicit val encoder: Encoder[FileInstance] = deriveEncoder[FileInstance]
}
