package org.plat.flowops.nova.tasks.web

import org.eclipse.jetty.server.Server
import java.io.File
import com.typesafe.scalalogging.LazyLogging
import org.eclipse.jetty.servlet.ServletHolder
import org.plat.flowops.nova.utils.EnvironmentLoader
import org.eclipse.jetty.servlet.{ ServletHandler, ServletHolder }
import org.plat.flowops.nova.constants.DefaultEnvironmentConstants
import org.plat.flowops.nova.servlet.GitHttpServlet

object ServerFactory extends LazyLogging:
  def createServer(port: Int, basePath: String, gitHttpServletPath: String): Server =

    logger.info("Starting Jetty Server with port {}", port)

    logger.info("Base Path: {}", basePath)

    createBasePathIfNotExists(basePath)

    val server: Server                 = new Server(port)
    val servletHolder: ServletHolder   = new ServletHolder(new GitHttpServlet)
    val servletHandler: ServletHandler = new ServletHandler

    servletHolder.setInitParameter("base-path", basePath)
    servletHolder.setInitParameter("export-all", "0")

    logger.info("Git HTTP Servlet Path: {}", gitHttpServletPath)

    servletHandler.addServletWithMapping(servletHolder, gitHttpServletPath)
    server.setHandler(servletHandler)
    server

  private def createBasePathIfNotExists(basePath: String): Unit =
    val basePathFile = new File(basePath)
    if !basePathFile.exists() then
      logger.debug("Base Path {} does not exist, hence creating one...", basePath)
      basePathFile.mkdirs()
