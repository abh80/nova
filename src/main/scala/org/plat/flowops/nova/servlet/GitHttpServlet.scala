package org.plat.flowops.nova.servlet

import com.typesafe.scalalogging.LazyLogging
import org.eclipse.jgit.http.server.GitServlet
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.transport.resolver.RepositoryResolver
import org.plat.flowops.nova.constants.InternalConstants
import org.plat.flowops.nova.registry.LockRegistry
import org.plat.flowops.nova.utils.{ HttpRequestUtil, HttpUtil }

import java.util.regex.Pattern
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

class GitHttpServlet extends GitServlet with LazyLogging:

  private var basePath: String = _

  def this(basePath: String) =
    this()
    this.basePath = basePath
    setRepositoryResolver(new GitRepositoryResolver(basePath))

  override def service(req: HttpServletRequest, res: HttpServletResponse): Unit =
    res.sendError(404)
//    if isGitRequest(req) then usingLockedRepository(req) { super.service(req, res) }
//    else res.sendError(HttpServletResponse.SC_NOT_FOUND)

  private def usingLockedRepository[T](req: HttpServletRequest)(f: => T): T =
    if req.getAttribute(InternalConstants.LOCKED_REPOSITORY_KEY) != null then
      LockRegistry.lock(req.getAttribute(InternalConstants.LOCKED_REPOSITORY_KEY).asInstanceOf[String]) {
        f
      }
    else f

class GitRepositoryResolver(storagePath: String) extends RepositoryResolver[HttpServletRequest]:
  def open(req: HttpServletRequest, name: String): Repository =
    val repositoryPath = s"$storagePath/$name"
    val user_base_path = name.split("/").head
    ???
