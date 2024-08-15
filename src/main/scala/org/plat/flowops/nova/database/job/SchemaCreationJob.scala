package org.plat.flowops.nova.database.job

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.constants.DefaultEnvironmentConstants
import org.plat.flowops.nova.constants.DefaultEnvironmentConstants.MAKE_FIXTURES
import org.plat.flowops.nova.database.MyPostgresProfile.api.*
import org.plat.flowops.nova.database.PostgresManager
import org.plat.flowops.nova.database.schema.*
import org.plat.flowops.nova.helper.JobRunner
import org.plat.flowops.nova.utils.EnvironmentLoader
import org.quartz.{Job, JobExecutionContext}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class SchemaCreationJob extends Job with LazyLogging:

  override def execute(jobExecutionContext: JobExecutionContext): Unit =
    val novaUser                   = TableQuery[NovaUserTable]
    val novaUserCreds              = TableQuery[NovaUserCredsTable]
    val novaRepository             = TableQuery[NovaRepositoryTable]
    val novaRepositoryAccessPolicy = TableQuery[NovaRepositoryAccessPolicyTable]
    val tables =
      novaUser.schema ++ novaUserCreds.schema ++ novaRepository.schema ++ novaRepositoryAccessPolicy.schema
    logger.debug("Creating Database Schemas!")

    val database = PostgresManager()
    val f        = database().run(DBIO.seq(tables.createIfNotExists).transactionally)

    f.onComplete {
      case Success(_) =>
        logger.debug("Tables created successfully!")
        if EnvironmentLoader.getEnvironmentVariable(MAKE_FIXTURES.toString, DefaultEnvironmentConstants.MAKE_FIXTURES).toBoolean then
          JobRunner.runOnce(classOf[FixtureCreationJob].getName)

      case Failure(e) => logger.error(e.getMessage)
    }
