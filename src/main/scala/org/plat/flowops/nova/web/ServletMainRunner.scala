package org.plat.flowops.nova.web

import com.typesafe.scalalogging.LazyLogging
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ServletHandler, ServletHolder}
import org.plat.flowops.nova.constants.DefaultEnvironmentConstants
import org.plat.flowops.nova.servlet.GitHttpServlet
import org.plat.flowops.nova.utils.EnvironmentLoader
import org.plat.flowops.nova.web.tasks.{ServerStartTask, Task}

object ServletMainRunner extends App with LazyLogging {
  logger.info("Starting Servlet Main Runner")
  
  private val startTask : Task = new ServerStartTask()
  startTask.execute()
}
