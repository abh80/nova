package org.plat.flowops.nova.utils

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.constants.DefaultEnvironmentConstants

object EnvironmentLoader extends LazyLogging:
  private val cachedEnvironmentVariables: collection.concurrent.Map[String, String] =
    collection.concurrent.TrieMap[String, String]()

  def getRequiredEnvironmentVariable(variable: String): String = getEnvironmentVariable(variable, null, true)

  def getEnvironmentVariable(
      variable: String,
      default: DefaultEnvironmentConstants,
      required: Boolean = false
  ): String =
    if cachedEnvironmentVariables.contains(variable) then return cachedEnvironmentVariables(variable)
    val returnable = Option(System.getenv(variable))
    if returnable.isEmpty then
      if required then
        logger.error("A crucial variable {} was not set", variable)
        throw new RuntimeException(s"Environment variable $variable is required but not set")
      else return default.value
    cachedEnvironmentVariables(variable) = returnable.get
    returnable.get
