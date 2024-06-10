package org.plat.flowops.nova.database

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.plat.flowops.nova.utils.EnvironmentLoader

import javax.sql.DataSource

object PostgresConfig {
  private var POSTGRES_URL: String = _
  private var POSTGRES_USER: String = _

  def createDatabaseConfig(password: String): DataSource =
    val hikariConfig = new HikariConfig()
    initStaticFieldsIfRequired()

    hikariConfig.setJdbcUrl(POSTGRES_URL)
    hikariConfig.setUsername(POSTGRES_USER)
    hikariConfig.setPassword(password)
    hikariConfig.setDriverClassName("org.postgresql.Driver")
    hikariConfig.setMaximumPoolSize(10)
    hikariConfig.setConnectionTestQuery("SELECT 1")
    new HikariDataSource(hikariConfig)

  private def initStaticFieldsIfRequired() : Unit =
    if POSTGRES_USER == null then POSTGRES_USER = EnvironmentLoader.getRequiredEnvironmentVariable("POSTGRES_USER")
    if POSTGRES_URL == null then POSTGRES_URL = EnvironmentLoader.getRequiredEnvironmentVariable("POSTGRES_URL")
}
