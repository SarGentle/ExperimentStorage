package ru.nir.testproject.services.tools

import com.zaxxer.hikari.HikariConfig
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor
import ru.nir.testproject.services.tools.config.DatabaseConfig
import zio.{Task, ZIO}
import zio.interop.catz._

package object database {

  def managedTransactor(
      conf: DatabaseConfig,
      appName: String
  ) = {
    for {
      ce <- ExecutionContexts.fixedThreadPool[Task](conf.threadPoolSize).toScopedZIO
      hikariConfig <- ZIO.attempt {
        val hikariConfig = new HikariConfig()
        hikariConfig.setDriverClassName(conf.driverClassName)
        hikariConfig.setJdbcUrl(conf.url)
        hikariConfig.setUsername(conf.username)
        hikariConfig.setPassword(conf.password)
        hikariConfig.setPoolName(appName)
        hikariConfig.setMaximumPoolSize(conf.maxPoolSize)
        hikariConfig.setConnectionTimeout(conf.connectionTimeoutMs)
        hikariConfig.addDataSourceProperty("socketTimeout", conf.socketTimeoutS)
        hikariConfig.setLeakDetectionThreshold(conf.leakDetectionThresholdMs)
        conf.dbScheme.foreach(hikariConfig.setSchema)
        hikariConfig
      }
      transactor <-
        HikariTransactor
          .fromHikariConfig[Task](
            hikariConfig,
            ce
          )
          .toScopedZIO
    } yield transactor

  }
}
