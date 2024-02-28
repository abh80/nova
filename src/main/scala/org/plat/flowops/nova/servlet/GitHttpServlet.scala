package org.plat.flowops.nova.servlet

import com.typesafe.scalalogging.LazyLogging
import org.eclipse.jgit.http.server.GitServlet

import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

class GitHttpServlet extends GitServlet with LazyLogging {
  private var basePath: String = _

  def this(basePath: String) = {
    this()
    this.basePath = basePath
  }

  override def service(req: HttpServletRequest, res: HttpServletResponse): Unit =
    logger.debug(s"GitHttpServlet.service(${req.toString}, ${res.toString})")
    res.getWriter.write("Hello, Git!")
}
