package org.plat.flowops.nova.utils

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.constants.DefaultEnvironmentConstants

object EnvironmentLoader extends LazyLogging:
  def getRequiredEnvironmentVariable(variable: String): String = getEnvironmentVariable(variable, null, true)

  def getEnvironmentVariable(
      variable: String,
      default: DefaultEnvironmentConstants,
      required: Boolean = false
  ): String =
    val returnable = Option(System.getenv(variable))
    if returnable.isEmpty then
      if required then
        logger.error("A crucial variable {} was not set", variable)
        throw new RuntimeException(s"Environment variable $variable is required but not set")
      else return default.value
    returnable.get
