package org.plat.flowops.nova.helper

import org.plat.flowops.nova.database.job.SchemaCreationJob
import org.plat.flowops.nova.service.{ JobRegistry, JobTriggerRegistry }
import org.quartz.impl.StdSchedulerFactory

object JobRunner:
  def runOnce(jobKey: String): Unit =
    val scheduler   = StdSchedulerFactory.getDefaultScheduler
    val job         = JobRegistry.get(classOf[SchemaCreationJob].getName).get
    val triggerOnce = JobTriggerRegistry.get(classOf[JRTriggerOnce].getName).get

    scheduler.scheduleJob(job, triggerOnce)
    scheduler.start()
