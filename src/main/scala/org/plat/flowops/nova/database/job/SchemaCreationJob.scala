package org.plat.flowops.nova.database.job

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.database.MyPostgresProfile.api.*
import org.plat.flowops.nova.database.PostgresManager
import org.plat.flowops.nova.database.schema.*
import org.quartz.{ Job, JobExecutionContext }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

class SchemaCreationJob extends Job with LazyLogging:

  override def execute(jobExecutionContext: JobExecutionContext): Unit =
    val novaUser      = TableQuery[NovaUserTable]
    val novaUserCreds = TableQuery[NovaUserCredsTable]
    val tables        = novaUser.schema ++ novaUserCreds.schema
    logger.debug("Creating Database Schemas!")
    val f = PostgresManager.getInstance.getDb.run(DBIO.seq(tables.createIfNotExists))

    f.onComplete {
      case Success(_) =>
        logger.debug("Tables created successfully!")
      case Failure(e) => throw e
    }
