package org.plat.flowops.nova.database.job

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.database.PostgresManager
import org.plat.flowops.nova.fixtures.{ MockRepository, MockUserCreds, MockUsers, MockRepositoryPolicy }
import org.quartz.{ Job, JobExecutionContext }

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{ Duration, DurationInt }

class FixtureCreationJob extends Job with LazyLogging:

  override def execute(jobExecutionContext: JobExecutionContext): Unit =
    logger.debug("Creating Fixtures!")
    val fixtures =
      List(classOf[MockUsers], classOf[MockUserCreds], classOf[MockRepository], classOf[MockRepositoryPolicy])
    fixtures.foreach(e =>
      Await.result(
        e.getDeclaredConstructor().newInstance().setDatabase(PostgresManager()).generateFixture(),
        5.minutes
      )
    )
    logger.debug("All Fixtures created successfully!")
