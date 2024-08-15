package org.plat.flowops.nova.helper

import org.plat.flowops.nova.registry.JobRegistry
import org.quartz.impl.StdSchedulerFactory

object JobRunner:
  def runOnce(jobKey: String): Unit =
    val scheduler   = StdSchedulerFactory.getDefaultScheduler
    val job         = JobRegistry.get(jobKey).get
    val triggerOnce = JobTriggerFactory.getTriggerOnce

    scheduler.scheduleJob(job, triggerOnce)
    scheduler.start()
