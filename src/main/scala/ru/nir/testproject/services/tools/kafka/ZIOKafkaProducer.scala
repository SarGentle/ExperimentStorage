package ru.nir.testproject.services.tools.kafka

import io.circe.Encoder
import org.apache.kafka.clients.producer.ProducerRecord
import ru.nir.testproject.services.tools.config.PropertiesConfig
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.serde.Serializer
import zio.{Scope, Task, ZIO}

final case class ZIOKafkaProducer(
    service: Producer,
    topic: String,
    keySerde: Serializer[Any, String],
    bodySer: Serializer[Any, String]
) {
  def sendOne(key: String, value: String): Task[Unit] =
    service.produce(new ProducerRecord(topic, key, value), keySerde, bodySer).unit
}

object ZIOKafkaProducer {
  def managed(
      servers: List[String],
      properties: PropertiesConfig,
      topic: String
  ): ZIO[Scope, Throwable, ZIOKafkaProducer] = {
    val propsMap = Map(
      "securityProtocol"      -> properties.securityProtocol,
      "sslTrustStoreLocation" -> properties.sslTrustStoreLocation,
      "sslTrustStorePassword" -> properties.sslTrustStorePassword,
      "sslKeyStoreLocation"   -> properties.sslKeyStoreLocation,
      "sslKeyStorePassword"   -> properties.sslKeyStorePassword,
      "sslKeyPassword"        -> properties.sslKeyPassword
    )
    val settings = ProducerSettings(servers)
    val jsonSerializer = Serializer.apply[Any, String] {
      case (_, _, data) => ZIO.attempt(Encoder[String].apply(data).noSpaces.getBytes("UTF-8"))
    }
    val valSer = jsonSerializer
    val keySer = jsonSerializer
    Producer.make(settings).map(p => ZIOKafkaProducer(p, topic, keySer, valSer))
  }
}
