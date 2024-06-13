package org.plat.flowops.nova.database

import com.zaxxer.hikari.{ HikariConfig, HikariDataSource }
import org.plat.flowops.nova.utils.EnvironmentLoader

import javax.sql.DataSource

object PostgresConfig:
  private var POSTGRES_URL: String = _

  def createDatabaseConfig(username: String, password: String): DataSource =
    val hikariConfig = new HikariConfig()
    initStaticFieldsIfRequired()

    hikariConfig.setJdbcUrl(POSTGRES_URL)
    hikariConfig.setUsername(username)
    hikariConfig.setPassword(password)
    hikariConfig.setDriverClassName("org.postgresql.Driver")
    hikariConfig.setMaximumPoolSize(10)
    hikariConfig.setConnectionTestQuery("SELECT 1")
    new HikariDataSource(hikariConfig)

  private def initStaticFieldsIfRequired(): Unit =
    if POSTGRES_URL == null then
      POSTGRES_URL = EnvironmentLoader.getRequiredEnvironmentVariable("POSTGRES_URL")
