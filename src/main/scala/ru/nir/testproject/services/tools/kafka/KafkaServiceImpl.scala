package ru.nir.testproject.services.tools.kafka

import ru.nir.testproject.{AppEnv, AppTask}
import ru.nir.testproject.services.tools.config.{DataConsumerProperties, PropertiesConfig}
import zio.{RIO, Ref, Scope, Task, ZIO}

class KafkaServiceImpl(
    producerMapRef: Ref[Map[String, ZIOKafkaProducer]],
    consumerMapRef: Ref[Map[String, ZIOKafkaConsumer]]
) extends KafkaService {

  override def createZIOKafkaProducer(
      servers: List[String],
      properties: PropertiesConfig,
      topic: String
  ): RIO[Scope, Unit] = {
    for {
      producer <- ZIOKafkaProducer.managed(servers, properties, topic)
      _        <- producerMapRef.update(_.updated(topic, producer))
    } yield ()
  }

  override def createZIOKafkaConsumer(
      servers: List[String],
      props: DataConsumerProperties,
      topic: String,
      f: String => AppTask[Unit]
  ): RIO[Scope with AppEnv, Unit] = {
    for {
      consumer <- ZIOKafkaConsumer.managed(servers, props, topic, f)
      _        <- consumerMapRef.update(_.updated(topic, consumer))
      _ <- (for {
          _ <- consumeStream(topic, 1)
        } yield ()).forkDaemon
    } yield ()
  }

  override def closeZIOKafkaConsumer(topic: String): Task[Unit] = {
    for {
      optionConsumer <- consumerMapRef.get.map(_.get(topic))
      consumer       <- ZIO.fromOption(optionConsumer).mapError(_ => new Exception("ZIOKafkaConsumer is not found"))
      _              <- consumer.service.stopConsumption
      _              <- consumerMapRef.update(_.removed(topic))
    } yield ()
  }

  override def sendOneMessage(topic: String, key: String, message: String): Task[Unit] = {
    for {
      optionProducer <- producerMapRef.get.map(_.get(topic))
      producer       <- ZIO.fromOption(optionProducer).mapError(_ => new Exception("ZIOKafkaProducer is not found"))
      _              <- producer.sendOne(key, message)
    } yield ()
  }

  private def consumeStream(topic: String, countOfChunks: Int): AppTask[Unit] = {
    for {
      optionConsumer <- consumerMapRef.get.map(_.get(topic))
      consumer       <- ZIO.fromOption(optionConsumer).mapError(_ => new Exception("ZIOKafkaConsumer is not found"))
      _              <- consumer.createStringStream.take(countOfChunks).runCollect
    } yield ()
  }
}
