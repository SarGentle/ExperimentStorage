package ru.nir.testproject.services.tools

import zio.ZIO

package object kafka {

  def init() = {
    for {
      kafkaConfig     <- config.getKafkaConfig
      kafkaService    <- ZIO.service[KafkaService]
      listOfProducers <- ZIO.attempt(kafkaConfig.resultProducer.topic)
      listOfConsumers <- ZIO.attempt(kafkaConfig.dataConsumer.topic)
      _ <- ZIO.foreach(listOfProducers)(topic => {
        for {
          _ <- kafkaService.createZIOKafkaProducer(
            kafkaConfig.servers,
            kafkaConfig.properties,
            topic
          )
        } yield ()
      })
      _ <- ZIO.foreach(listOfConsumers)(topic => {
        for {
          _ <- kafkaService.createZIOKafkaConsumer(
            kafkaConfig.servers,
            kafkaConfig.dataConsumer.properties,
            topic,
            (s: String) => zio.Console.printLine(s)
          )
        } yield ()
      })
    } yield ()
  }
}
