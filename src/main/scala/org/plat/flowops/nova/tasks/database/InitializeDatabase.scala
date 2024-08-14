package org.plat.flowops.nova.tasks.database

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.database.{ PostgresConfig, PostgresManager }
import org.plat.flowops.nova.tasks.Task
import org.plat.flowops.nova.tasks.injector.InjectorHandler
import org.plat.flowops.nova.utils.EnvironmentLoader

object InitializeDatabase extends Task with LazyLogging:

  override def execute(): Unit = new Thread(this).start()

  override def run(): Unit =
    val postgresManager = PostgresManager()

    logger.info("Initializing Database without Vault...")

    val postgresUser     = EnvironmentLoader.getRequiredEnvironmentVariable("POSTGRES_USER")
    val postgresPassword = EnvironmentLoader.getRequiredEnvironmentVariable("POSTGRES_PASSWORD")
    val datasource       = PostgresConfig.createDatabaseConfig(postgresUser, postgresPassword)

    postgresManager.connect(datasource)
