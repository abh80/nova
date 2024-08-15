package org.plat.flowops.nova.helper

import org.quartz.{ SimpleScheduleBuilder, Trigger, TriggerBuilder }

object JobTriggerFactory:
  def getTriggerOnce: Trigger =
    TriggerBuilder
      .newTrigger()
      .withIdentity(classOf[JRTriggerOnce].getName)
      .startNow()
      .withSchedule(SimpleScheduleBuilder.simpleSchedule())
      .build()
