package org.plat.flowops.nova.tasks.scheduler

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.database.job.SchemaCreationJob
import org.plat.flowops.nova.helper.JRTriggerOnce
import org.plat.flowops.nova.service.{ JobRegistry, JobTriggerRegistry }
import org.plat.flowops.nova.tasks.Task
import org.quartz.{ Job, JobBuilder, SimpleScheduleBuilder, TriggerBuilder }

object Scheduler extends Task with LazyLogging:
  override def execute(): Unit =
    val jobs: List[Class[? <: Job]] = List(classOf[SchemaCreationJob])

    jobs.foreach(job =>
      val jobDetail = JobBuilder.newJob(job).withIdentity(job.getName).build()
      JobRegistry.register(job.getName, jobDetail)
      logger.debug("Registered a Job: {}", job.getName)
    )

    val triggerOnce = TriggerBuilder
      .newTrigger()
      .withIdentity(classOf[JRTriggerOnce].getName)
      .startNow()
      .withSchedule(SimpleScheduleBuilder.simpleSchedule())
      .build()

    JobTriggerRegistry.register(classOf[JRTriggerOnce].getName, triggerOnce)
  override def run(): Unit = ()
