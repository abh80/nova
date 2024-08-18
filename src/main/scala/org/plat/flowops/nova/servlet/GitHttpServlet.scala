package org.plat.flowops.nova.servlet

import com.typesafe.scalalogging.LazyLogging
import org.eclipse.jetty.http.HttpStatus
import org.eclipse.jgit.http.server.GitServlet
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.transport.ReceivePack
import org.eclipse.jgit.transport.resolver.{ ReceivePackFactory, RepositoryResolver }
import org.plat.flowops.nova.constants.InternalConstants
import org.plat.flowops.nova.database.schema.{ NovaRepository, NovaUser }
import org.plat.flowops.nova.helper.RepositoryFactory
import org.plat.flowops.nova.registry.LockRegistry

import javax.servlet.ServletConfig
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

class GitHttpServlet extends GitServlet with LazyLogging:

  override def init(config: ServletConfig): Unit =
    setRepositoryResolver(new GitRepositoryResolver)
    setReceivePackFactory(new GitReceivePackFactory)
    super.init(config)

  override def service(req: HttpServletRequest, res: HttpServletResponse): Unit =
    logger.debug("Git Servlet")
    if req.getAttribute(InternalConstants.REPOSITORY_KEY) == null then
      logger.error("Repository Key is missing! Illegal")
      res.sendError(HttpStatus.INTERNAL_SERVER_ERROR_500)
      req.getAsyncContext.complete()
      return

    usingLockedRepository(req) {
      super.service(req, res)
      req.getAsyncContext.complete()
    }

  private def usingLockedRepository[T](req: HttpServletRequest)(f: => T): T =
    if req.getAttribute(InternalConstants.LOCKED_REPOSITORY_KEY) != null then
      LockRegistry.lock(req.getAttribute(InternalConstants.LOCKED_REPOSITORY_KEY).asInstanceOf[String]) {
        f
      }
    else f

class GitRepositoryResolver extends RepositoryResolver[HttpServletRequest] with RepositoryFactory:

  def open(req: HttpServletRequest, name: String): Repository =
    val repo = req.getAttribute(InternalConstants.REPOSITORY_KEY).asInstanceOf[NovaRepository]
    getRepository(repo.owner_id, repo.repository_id.get)

class GitReceivePackFactory extends ReceivePackFactory[HttpServletRequest] with LazyLogging:

  override def create(req: HttpServletRequest, repository: Repository): ReceivePack =
    val receivePack = new ReceivePack(repository)

    val pusher = req.getAttribute(InternalConstants.USER_KEY).asInstanceOf[NovaUser]
    val repo   = req.getAttribute(InternalConstants.REPOSITORY_KEY).asInstanceOf[NovaRepository]

    if pusher == null then
      receivePack.sendError("User is missing")
      return receivePack

    logger.debug(s"Request URI: ${req.getRequestURI}")
    logger.debug(s"Pusher: ${pusher.username}")
    logger.debug(s"Repository: ${repo.owner_id}/${repo.repository_id.get}")

    receivePack
