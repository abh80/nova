package org.plat.flowops.nova

import com.typesafe.scalalogging.LazyLogging
import org.eclipse.jetty.server.Server
import org.plat.flowops.nova.constants.DefaultEnvironmentConstants
import org.plat.flowops.nova.servlet.GitHttpServlet
import org.plat.flowops.nova.utils.EnvironmentLoader
import org.eclipse.jetty.servlet.{ServletHolder, ServletHandler}

object ServletMainRunner extends App with LazyLogging {
  private def createBasePathIfNotExists(basePath: String): Unit = {
    val basePathFile = new java.io.File(basePath)
    if (!basePathFile.exists()) {
      logger.debug("Base Path {} does not exist, hence creating one...", basePath)
      basePathFile.mkdirs()
    }
  }

  logger.info("Starting Servlet Main Runner")

  private val port: Int = EnvironmentLoader.getEnvironmentVariable("PORT", DefaultEnvironmentConstants.PORT).toInt
  logger.info("Starting Jetty Server with port {}", port)

  private val basePath: String = EnvironmentLoader.getEnvironmentVariable("BASE_PATH", DefaultEnvironmentConstants.BASE_PATH)
  logger.info("Base Path: {}", basePath)

  createBasePathIfNotExists(basePath)

  val server: Server = new Server(port)
  private val servletHolder: ServletHolder = new ServletHolder(new GitHttpServlet)
  private val servletHandler: ServletHandler = new ServletHandler

  servletHolder.setInitParameter("base-path", basePath)
  servletHolder.setInitParameter("export-all", "0");

  private val gitHttpServletPath: String = EnvironmentLoader.getEnvironmentVariable("GIT_HTTP_SERVLET_PATH", DefaultEnvironmentConstants.GIT_HTTP_SERVLET_PATH)
  logger.info("Git HTTP Servlet Path: {}", gitHttpServletPath)

  servletHandler.addServletWithMapping(servletHolder, gitHttpServletPath)
  server.setHandler(servletHandler)

  server.start()
  logger.info("Server started at port {}", port)

  server.join()
}
