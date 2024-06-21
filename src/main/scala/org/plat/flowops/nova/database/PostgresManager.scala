package org.plat.flowops.nova.database

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.constants.DatabaseTypes
import org.plat.flowops.nova.listeners.events.{
  DatabaseConnectedEvent,
  DatabaseDisconnectedEvent,
  DatabaseErrorEvent
}
import org.plat.flowops.nova.listeners.{ Event, EventListenerAdapter }
import slick.jdbc.PostgresProfile.api.*

import javax.sql.DataSource

class PostgresManager(eventListener: EventListenerAdapter) extends TDatabaseManager with LazyLogging:
  @volatile private var db: Database = _

  /** Connects to the database using the provided DataSource.
    *
    * @param dataSource
    *   the DataSource to connect to
    */
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

  /** Closes the current database connection if it exists.
    */
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

  /** Returns the type of the database.
    *
    * @return
    *   the database type as a String
    */
  override def getType: String = DatabaseTypes.TYPE_PSQL

  /** Checks if database is connected
    * @return
    *   true if database is connected else returns false
    */
  override def isConnected: Boolean = synchronized(db != null)

  def getDb: Database = db

object PostgresManager:
  @volatile private var instance: PostgresManager = _

  /** @param eventListener
    *   event listener adapter for the instance
    * @return
    *   the manager for postgres database
    */
  def apply(eventListener: EventListenerAdapter): PostgresManager =
    if instance == null then
      synchronized {
        if instance == null then instance = new PostgresManager(eventListener)
      }
    instance

  /** @return
    *   the manager for postgres database if initialized
    */
  def getInstance: PostgresManager =
    if instance == null then
      throw new IllegalStateException("Instance not initialized. Call apply(eventListener) first.")
    instance
