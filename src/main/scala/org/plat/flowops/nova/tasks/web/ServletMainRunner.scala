package org.plat.flowops.nova.tasks.web

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.tasks.Task
import org.plat.flowops.nova.tasks.web.tasks.ServerStartTask

object ServletMainRunner extends LazyLogging with Task:
  override def run(): Unit = ()

  override def execute(): Unit = startServlet()

  private def startServlet(): Unit =
    logger.info("Starting Servlet Main Runner")

    val serverStartTask: Task = new ServerStartTask()
    serverStartTask.execute()
