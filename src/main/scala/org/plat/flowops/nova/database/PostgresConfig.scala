package org.plat.flowops.nova.database

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.plat.flowops.nova.utils.EnvironmentLoader

import javax.sql.DataSource

object PostgresConfig {
  def createDatabaseConfig(password: String): DataSource =
    val hikariConfig = new HikariConfig()
    hikariConfig.setJdbcUrl(EnvironmentLoader.getRequiredEnvironmentVariable("POSTGRES_URL"))
    hikariConfig.setUsername(EnvironmentLoader.getRequiredEnvironmentVariable("POSTGRES_USER"))
    hikariConfig.setPassword(password)
    hikariConfig.setDriverClassName("org.postgresql.Driver")
    hikariConfig.setMaximumPoolSize(10)
    hikariConfig.setConnectionTestQuery("SELECT 1")
    new HikariDataSource(hikariConfig)
}
