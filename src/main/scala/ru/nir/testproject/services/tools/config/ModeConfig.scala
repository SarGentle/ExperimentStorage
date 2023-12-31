package ru.nir.testproject.services.tools.config


case class ModeConfig(
    testMode: Option[String]
)

case class AppModeParameters(
    testMode: String
) {
  def tags: List[(String, String)] =
    List(
      "test_mode" -> testMode
    ).map(t => (t._1, t._2.toString))

}
