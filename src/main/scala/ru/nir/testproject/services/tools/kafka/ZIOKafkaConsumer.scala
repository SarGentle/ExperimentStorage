package ru.nir.testproject.services.tools.kafka

import ru.nir.testproject.{AppEnv, AppTask}
import ru.nir.testproject.services.tools.config.DataConsumerProperties
import zio.{Scope, ZIO}
import zio.kafka.serde.{Deserializer, Serde}
import zio.kafka.consumer._
import zio.stream.ZStream

case class ZIOKafkaConsumer(
    service: Consumer,
    topic: String,
    keyDeserializer: Deserializer[Any, String],
    valueDeserializer: Deserializer[Any, String],
    procFunc: String => AppTask[Unit]
) {

  def createStringStream: ZStream[AppEnv, Throwable, Nothing] =
    service
      .plainStream(Subscription.topics(topic), keyDeserializer, valueDeserializer)
      .mapZIO(msg =>
        procFunc(msg.value).foldZIO(
          err => zio.Console.printLine(s"--------------ERROR-------------- Cause: ${err.getMessage}"),
          _ => msg.offset.commit
        )
      )
      .drain
}

object ZIOKafkaConsumer {
  def managed(
      servers: List[String],
      props: DataConsumerProperties,
      topic: String,
      f: String => AppTask[Unit]
  ): ZIO[Scope, Throwable, ZIOKafkaConsumer] = {
    val propsMap = Map(
      "group.id"          -> props.groupId,
      "auto.offset.reset" -> props.autoOffsetReset
    )
    Consumer
      .make(
        ConsumerSettings(
          bootstrapServers = servers
        ).withRestartStreamOnRebalancing(true)
          .withProperties(propsMap)
      )
      .map(service =>
        ZIOKafkaConsumer(
          service,
          topic,
          Serde.string,
          Serde.string,
          f
        )
      )
  }

}

final case class CommittableValue[T](value: T, offset: Offset)
