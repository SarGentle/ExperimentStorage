package ru.nir.testproject.services.tools.kafka

import ru.nir.testproject.{AppEnv, AppTask}
import ru.nir.testproject.services.tools.config.{DataConsumerProperties, PropertiesConfig}
import zio.{RIO, Scope, Task, ZIO, ZLayer, Ref}

trait KafkaService {

  def createZIOKafkaProducer(
      servers: List[String],
      properties: PropertiesConfig,
      topic: String
  ): RIO[Scope, Unit]

  def createZIOKafkaConsumer(
      servers: List[String],
      props: DataConsumerProperties,
      topic: String,
      f: String => AppTask[Unit]
  ): RIO[Scope with AppEnv, Unit]

  def closeZIOKafkaConsumer(topic: String): Task[Unit]
  def sendOneMessage(topic: String, key: String, message: String): Task[Unit]
  //  def consumeStream(topic: String, countOfChunks: Int): AppTask[Unit]
}

object KafkaService {

  def createZIOKafkaProducer(
      servers: List[String],
      properties: PropertiesConfig,
      topic: String
  ): RIO[KafkaService with Scope, Unit] =
    ZIO.environmentWithZIO[KafkaService](_.get.createZIOKafkaProducer(servers, properties, topic))

  def createZIOKafkaConsumer(
      servers: List[String],
      props: DataConsumerProperties,
      topic: String,
      f: String => AppTask[Unit]
  ): RIO[AppEnv with Scope, Unit] =
    ZIO.environmentWithZIO[KafkaService](_.get.createZIOKafkaConsumer(servers, props, topic, f))

  def closeZIOKafkaConsumer(topic: String): RIO[KafkaService, Unit] =
    ZIO.environmentWithZIO[KafkaService](_.get.closeZIOKafkaConsumer(topic))

  def sendOneMessage(topic: String, key: String, message: String): RIO[KafkaService, Unit] =
    ZIO.environmentWithZIO[KafkaService](_.get.sendOneMessage(topic, key, message))

  //  def consumeStream(topic: String, countOfChunks: Int): RIO[AppEnv, Unit] =
  //    ZIO.environmentWithZIO[KafkaService](_.get.consumeStream(topic, countOfChunks))
}

object KafkaServiceLive {
  def layer =
    ZLayer {
      for {
        producerMapRef <- Ref.make(Map.empty[String, ZIOKafkaProducer])
        consumerMapRef <- Ref.make(Map.empty[String, ZIOKafkaConsumer])
      } yield new KafkaServiceImpl(producerMapRef, consumerMapRef)
    }
}
