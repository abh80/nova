package org.plat.flowops.nova.listeners.actionables
import org.plat.flowops.nova.database.{ PostgresConfig, PostgresManager }

import java.util

class PostgresDatasourceLeaseListener extends CustomLeaseActionable:
  override def onLeaseExpired(): Unit = ()

  override def onLeaseCreated(secrets: util.Map[String, String]): Unit =
    val username = secrets.get("username")
    val password = secrets.get("password")
    val config   = PostgresConfig.createDatabaseConfig(username, password)
    PostgresManager.connect(config)
