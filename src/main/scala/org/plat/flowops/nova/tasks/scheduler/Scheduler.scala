package org.plat.flowops.nova.tasks.scheduler

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.database.job.{FixtureCreationJob, SchemaCreationJob}
import org.plat.flowops.nova.registry.JobRegistry
import org.plat.flowops.nova.tasks.Task
import org.quartz.{Job, JobBuilder}

object Scheduler extends Task with LazyLogging:
  override def execute(): Unit =
    val jobs: List[Class[? <: Job]] = List(classOf[SchemaCreationJob], classOf[FixtureCreationJob])

    jobs.foreach(job =>
      val jobDetail = JobBuilder.newJob(job).withIdentity(job.getName).build()
      JobRegistry.register(job.getName, jobDetail)
      logger.debug("Registered a Job: {}", job.getName)
    )

  override def run(): Unit = ()
