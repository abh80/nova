package org.plat.flowops.nova.utils

class Snowflake(workerId: Long, datacenterId: Long, var sequence: Long = 0L):
  val epoch             = 1288834974657L
  val workerIdBits      = 5L
  val datacenterIdBits  = 5L
  val maxWorkerId       = -1L ^ (-1L << workerIdBits)
  val maxDatacenterId   = -1L ^ (-1L << datacenterIdBits)
  val sequenceBits      = 12L
  val workerIdShift     = sequenceBits
  val datacenterIdShift = sequenceBits + workerIdBits
  val timestampShift    = sequenceBits + workerIdBits + datacenterIdBits
  val sequenceMask      = -1L ^ (-1L << sequenceBits)

  // Ensure that workerId and datacenterId are within allowed ranges
  require(
    workerId >= 0 && workerId <= maxWorkerId,
    s"worker Id can't be greater than $maxWorkerId or less than 0"
  )
  require(
    datacenterId >= 0 && datacenterId <= maxDatacenterId,
    s"datacenter Id can't be greater than $maxDatacenterId or less than 0"
  )

  var lastTimestamp = -1L

  def generateId(): Long = synchronized {
    val timestamp = currentTimestamp()

    if timestamp < lastTimestamp then
      throw new RuntimeException(
        s"Clock moved backwards. Refusing to generate id for ${lastTimestamp - timestamp} milliseconds"
      )

    if lastTimestamp == timestamp then
      sequence = (sequence + 1) & sequenceMask
      if sequence == 0 then
        // Sequence overflow, wait until the next millisecond
        while timestamp <= lastTimestamp do Thread.sleep(1)
    else sequence = 0

    lastTimestamp = timestamp

    ((timestamp - epoch) << timestampShift) |
      (datacenterId << datacenterIdShift) |
      (workerId << workerIdShift) |
      sequence
  }

  private def currentTimestamp(): Long = System.currentTimeMillis()
