package ru.nir.testproject.model.database

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import java.time.LocalDate

case class Experiment(
    experimentID: Int,
    experimentName: String,
    experimentDesc: String,
    experimentOrg: String,
    experimentAuthor: String,
    experimentDate: LocalDate
)

object Experiment {
  implicit val decoder: Decoder[Experiment] = deriveDecoder[Experiment]
  implicit val encoder: Encoder[Experiment] = deriveEncoder[Experiment]
}
