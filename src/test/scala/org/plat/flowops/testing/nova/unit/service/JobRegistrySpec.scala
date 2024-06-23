package org.plat.flowops.testing.nova.unit.service

import org.plat.flowops.nova.service.JobRegistry
import org.quartz.{JobBuilder, JobDetail}
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class JobRegistrySpec extends AnyFlatSpec with MockFactory with Matchers:
  "JobRegistry" should "register, get, remove and list all keys correctly" in {
    val jobDetail: JobDetail = mock[JobDetail]
    JobRegistry.register("testKey", jobDetail)

    JobRegistry.get("testKey") should be(Some(jobDetail))

    JobRegistry.getAllKeys should contain("testKey")

    JobRegistry.remove("testKey") should be(Some(jobDetail))

    JobRegistry.get("testKey") should be(None)
  }
