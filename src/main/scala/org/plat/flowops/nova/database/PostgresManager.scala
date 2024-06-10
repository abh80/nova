package org.plat.flowops.nova.database

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.constants.DatabaseTypes
import slick.jdbc.PostgresProfile.api.*

import javax.sql.DataSource

object PostgresManager extends TDatabaseManager with LazyLogging {
  @volatile private var db: Database = _

  override def connect(dataSource: DataSource): Unit =
    logger.debug(s"${if db != null then "Updating the" else "Creating a new"} database connection...")
    if db != null then closeConnection()
    synchronized {
      db = Database.forDataSource(dataSource, Some(10))
      logger.info("Database connection created")
    }

  override def closeConnection(): Unit = synchronized {
    db.close()
    logger.info("Closing the database connection")
  }

  override def getType: String = DatabaseTypes.TYPE_PSQL
}
