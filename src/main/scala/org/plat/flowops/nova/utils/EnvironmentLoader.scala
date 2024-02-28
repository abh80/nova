package org.plat.flowops.nova.utils

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.constants.DefaultEnvironmentConstants

//
object EnvironmentLoader extends LazyLogging {
  def getEnvironmentVariable(variable: String, default: DefaultEnvironmentConstants, required: Boolean = false): String = {
    val returnable = Option(System.getenv(variable))
    if (returnable.isEmpty) then
      if required then logger.warn("A crucial variable {} was not set", variable)
      else return default.value
    returnable.get
  }
}
