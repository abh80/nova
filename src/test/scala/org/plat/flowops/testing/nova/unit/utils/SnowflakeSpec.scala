package utils

import org.plat.flowops.nova.utils.Snowflake
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SnowflakeSpec extends AnyFlatSpec with Matchers:

  "Snowflake" should "generate unique IDs" in {
    val workerId     = 1L
    val datacenterId = 1L
    val snowflake    = new Snowflake(workerId, datacenterId)

    val id1 = snowflake.generateId()
    val id2 = snowflake.generateId()

    (id1 should not).equal(id2)
  }

  it should "generate a large number of unique IDs" in {
    val workerId     = 1L
    val datacenterId = 1L
    val snowflake    = new Snowflake(workerId, datacenterId)
    val idSet        = scala.collection.mutable.Set[Long]()

    for _ <- 1 to 100 do
      val id = snowflake.generateId()
      idSet.contains(id) should be(false)
      idSet.add(id)
  }

  it should "handle sequence overflow within the same millisecond" in {
    val workerId     = 1L
    val datacenterId = 1L
    val snowflake    = new Snowflake(workerId, datacenterId)
    snowflake.sequence = snowflake.sequenceMask

    val id1 = snowflake.generateId()
    val id2 = snowflake.generateId()

    (id1 should not).equal(id2)
  }

  it should "throw an exception when the clock moves backwards" in {
    val workerId     = 1L
    val datacenterId = 1L
    val snowflake    = new Snowflake(workerId, datacenterId)
    snowflake.lastTimestamp = System.currentTimeMillis() + 1

    val exception = intercept[RuntimeException] {
      snowflake.generateId()
    }
    exception.getMessage should include("Clock moved backwards")
  }
