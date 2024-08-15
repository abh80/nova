package org.plat.flowops.nova.database.job

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.fixtures.MockUsers
import org.quartz.{ Job, JobExecutionContext }

import scala.concurrent.ExecutionContext.Implicits.global

class FixtureCreationJob extends Job with LazyLogging:

  override def execute(jobExecutionContext: JobExecutionContext): Unit =
    logger.debug("Creating Fixtures!")
    val fixtures = List(classOf[MockUsers])
    fixtures.foreach(_.getDeclaredConstructor().newInstance().generateFixture())
