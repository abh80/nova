package org.plat.flowops.nova.database

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.constants.DatabaseTypes

import java.sql.{Connection, DriverManager}

object PostgresManager extends TDatabaseManager with LazyLogging {
  private var connection: Connection = _

  override def connect(connectionURL: String, username: String, password: String): Unit =
    logger.debug(s"Connecting to $connectionURL, username: $username, password: $password")
    connection = DriverManager.getConnection(connectionURL, username, password)
  end connect

  override def closeConnection(): Unit = connection.close()

  override def getType: String = DatabaseTypes.TYPE_PSQL
}