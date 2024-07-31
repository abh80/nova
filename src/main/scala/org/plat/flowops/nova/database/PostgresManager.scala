package org.plat.flowops.nova.database

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.constants.{ DatabaseTypes, InternalConstants }
import org.plat.flowops.nova.listeners.events.*
import org.plat.flowops.nova.listeners.{ Event, EventListenerAdapter }
import slick.jdbc.JdbcBackend.{ BaseSession, Database, Session }
import slick.jdbc.PostgresProfile.api.*

import javax.servlet.http.HttpServletRequest
import javax.sql.DataSource

class PostgresManager @Inject() (eventListener: EventListenerAdapter)
    extends TDatabaseManager
    with LazyLogging:
  @volatile private var db: Database = _

  override def connect(dataSource: DataSource): Unit =
    logger.debug(s"${if db != null then "Updating the" else "Creating a new"} database connection...")
    if db != null then closeConnection()
    synchronized {
      try
        db = Database.forDataSource(dataSource, Some(10))
        logger.info("Database connection created")
        eventListener.onEvent(Event(new DatabaseConnectedEvent, None))
      catch
        case ex: Exception =>
          logger.error("Failed to create database connection", ex)
          eventListener.onEvent(Event(new DatabaseErrorEvent, None))
          throw ex
    }

  override def closeConnection(): Unit = synchronized {
    if db != null then
      try
        db.close()
        logger.info("Database connection closed")
        eventListener.onEvent(Event(new DatabaseDisconnectedEvent, None))
      catch
        case ex: Exception =>
          logger.error("Failed to close database connection", ex)
          eventListener.onEvent(Event(new DatabaseErrorEvent, None))
          throw ex
      finally db = null
  }

  override def getType: String = DatabaseTypes.TYPE_PSQL

  override def isConnected: Boolean = synchronized(db != null)

  def getDb: Database = db

  def apply(): Database = db
