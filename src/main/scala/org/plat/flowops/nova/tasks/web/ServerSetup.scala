package org.plat.flowops.nova.tasks.web

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.constants.DefaultEnvironmentConstants
import org.plat.flowops.nova.service.Directory
import org.plat.flowops.nova.utils.EnvironmentLoader

object ServerSetup extends LazyLogging:
  def setupAndStart(): Unit =
    val port: Int = EnvironmentLoader.getEnvironmentVariable("PORT", DefaultEnvironmentConstants.PORT).toInt
    val gitHttpServletPath: String = EnvironmentLoader.getEnvironmentVariable(
      "GIT_HTTP_SERVLET_PATH",
      DefaultEnvironmentConstants.GIT_HTTP_SERVLET_PATH
    )

    val server = ServerFactory.createServer(port, Directory.NOVA_HOME, gitHttpServletPath)
    server.start()
    server.join()
