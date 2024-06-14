package org.plat.flowops.nova.listeners.actionables
import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.database.{ PostgresConfig, PostgresManager }
import org.springframework.vault.core.lease.SecretLeaseContainer
import org.springframework.vault.core.lease.domain.RequestedSecret

import java.util

class PostgresDatasourceLeaseListener extends CustomLeaseActionable with LazyLogging:
  override def onLeaseExpired(
      secretLeaseContainer: SecretLeaseContainer,
      requestedSecret: RequestedSecret
  ): Unit =
    secretLeaseContainer.requestRotatingSecret(requestedSecret.getPath)

  override def onLeaseCreated(secrets: util.Map[String, String]): Unit =
    val username = secrets.get("username")
    val password = secrets.get("password")
    logger.debug(s"Renewing database connection with ${username}:${password}")
    val config = PostgresConfig.createDatabaseConfig(username, password)
    PostgresManager.connect(config)
