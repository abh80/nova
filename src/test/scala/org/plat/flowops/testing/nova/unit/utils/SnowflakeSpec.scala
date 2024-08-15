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

  it should "reset sequence when the timestamp changes" in {
    val workerId     = 1L
    val datacenterId = 1L
    val snowflake    = new Snowflake(workerId, datacenterId)

    // Generate an ID and store its timestamp part
    val id1        = snowflake.generateId()
    val timestamp1 = (id1 >> 22) + 1288834974657L

    // Force the system clock to move forward and generate another ID
    Thread.sleep(1)
    val id2        = snowflake.generateId()
    val timestamp2 = (id2 >> 22) + 1288834974657L

    // The two timestamps should be different
    (timestamp1 should not).equal(timestamp2)
    // The sequence should reset, so IDs should not match
    (id1 should not).equal(id2)
  }
