package ru.nir.testproject.services.tools.config

final case class KafkaConfig(
    servers: List[String],
    properties: PropertiesConfig,
    resultProducer: ResultProducer,
    dataConsumer: DataConsumer
)

case class PropertiesConfig(
    securityProtocol: String,
    sslTrustStoreLocation: String,
    sslTrustStorePassword: String,
    sslKeyStoreLocation: String,
    sslKeyStorePassword: String,
    sslKeyPassword: String
)

case class DataConsumerProperties(
    groupId: String,
    autoOffsetReset: String
)

case class ResultProducer(
    topic: List[String]
)

case class DataConsumer(
    topic: List[String],
    properties: DataConsumerProperties
)
