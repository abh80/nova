package org.plat.flowops.nova.database

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.constants.DatabaseTypes
import org.plat.flowops.nova.listeners.events.*
import org.plat.flowops.nova.listeners.{ Event, EventListenerAdapter }
import org.plat.flowops.nova.tasks.injector.InjectorHandler
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api.*

import javax.sql.DataSource
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success }

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

        checkDatabaseConnection.onComplete {
          case Success(_) =>
            logger.info("Database connection created")
            eventListener.onEvent(Event(new DatabaseConnectedEvent, None))
          case Failure(e) =>
            eventListener.onEvent(Event(new DatabaseErrorEvent, e))
        }

      catch
        case ex: Exception =>
          logger.error("Failed to create database connection", ex)
          eventListener.onEvent(Event(new DatabaseErrorEvent, None))
          throw ex
    }
  private def checkDatabaseConnection(implicit ec: ExecutionContext): Future[Unit] =
    db.run(sql"SELECT 1".as[Int].headOption)
      .map {
        case Some(r) => ()
        case None =>
          throw new RuntimeException("Failed to establish database connection.")
      }
      .recover { case ex: Exception =>
        logger.error(s"Error connecting to the database: ${ex.getMessage}")
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

object PostgresManager:
  @volatile private var instance: PostgresManager = _

  def apply(): PostgresManager =
    if instance == null then
      synchronized {
        instance = InjectorHandler.getInjector.getInstance(classOf[PostgresManager])
      }
    instance
