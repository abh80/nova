package org.plat.flowops.nova.servlet

import com.typesafe.scalalogging.LazyLogging
import org.eclipse.jgit.http.server.GitServlet
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.transport.resolver.RepositoryResolver
import org.plat.flowops.nova.utils.HttpRequestUtil

import javax.servlet.annotation.WebServlet
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

class GitHttpServlet extends GitServlet with LazyLogging:
  private var basePath: String = _

  def this(basePath: String) =
    this()
    this.basePath = basePath
    setRepositoryResolver(new GitRepositoryResolver())

  override def service(req: HttpServletRequest, res: HttpServletResponse): Unit =
    logger.whenDebugEnabled {
      logger.debug(s"Request Received: ${HttpRequestUtil.getDebugInfo(req)}")
    }
    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
    res.setHeader("WWW-Authenticate", "Basic realm=\"Git\"")

class GitRepositoryResolver extends RepositoryResolver[HttpServletRequest]:
  def open(req: HttpServletRequest, name: String): Repository = ???
