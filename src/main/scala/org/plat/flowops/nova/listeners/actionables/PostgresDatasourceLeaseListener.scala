package org.plat.flowops.nova.listeners.actionables
import org.plat.flowops.nova.database.{PostgresConfig, PostgresManager}

import java.util

class PostgresDatasourceLeaseListener extends CustomLeaseActionable {
  override def onLeaseExpired(): Unit = ()

  override def onLeaseCreated(secrets: util.Map[String, String]): Unit =
    val password = secrets.get("password")
    val config = PostgresConfig.createDatabaseConfig(password)
    PostgresManager.connect(config)
}
