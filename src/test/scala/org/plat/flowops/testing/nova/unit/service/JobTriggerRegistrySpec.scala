package org.plat.flowops.testing.nova.unit.service

import org.plat.flowops.nova.service.JobTriggerRegistry
import org.quartz.{ SimpleScheduleBuilder, Trigger, TriggerBuilder }
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class JobTriggerRegistrySpec extends AnyFlatSpec with Matchers:
  "JobTriggerRegistry" should "register, get, remove and list all keys correctly" in {
    val trigger: Trigger = TriggerBuilder
      .newTrigger()
      .withIdentity("testTrigger")
      .withSchedule(SimpleScheduleBuilder.simpleSchedule())
      .build()

    JobTriggerRegistry.register("testKey", trigger)

    JobTriggerRegistry.get("testKey") should be(Some(trigger))

    JobTriggerRegistry.getAllKeys should contain("testKey")

    JobTriggerRegistry.remove("testKey") should be(Some(trigger))

    JobTriggerRegistry.get("testKey") should be(None)
  }
