package org.plat.flowops.nova.database

import com.typesafe.scalalogging.LazyLogging

import java.sql.{Connection, DriverManager}

class PostgresManager extends LazyLogging {
  private var connection: Connection = _

  def this(connectionUrl: String, username: String, password: String) =
    this()
    logger.debug(s"Connecting to $connectionUrl, username: $username, password: $password")
    logger.info(s"Connecting to Postgres database")
    connection = DriverManager.getConnection(connectionUrl, username, password)
  end this

  private val getConnection: Connection = connection

  private def closeConnection(): Unit = connection.close()
}

object PostgresManager {
  private var instance: Option[PostgresManager] = None

  def getInstance(connectionUrl: String, username: String, password: String): PostgresManager =
    if (instance.isEmpty) {
      instance = Some(new PostgresManager(connectionUrl, username, password))
    }
    instance.get
  end getInstance

  def closeConnection(): Unit =
    instance.get.closeConnection()
    instance = None
  end closeConnection
}