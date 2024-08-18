package org.plat.flowops.nova.servlet

import org.plat.flowops.nova.constants.{ InternalConstants, RepositoryAccessLevelType }
import org.plat.flowops.nova.database.schema.{ NovaRepository, NovaUser }
import org.plat.flowops.nova.exception.{ RequestRejectionException, RequestRejectionExceptionType }
import org.plat.flowops.nova.service.RepositoryService
import org.plat.flowops.nova.utils.{ HttpFilter, HttpUtil }
import slick.jdbc.PostgresProfile.api.*

import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

class RepositoryFilter extends RepositoryService with HttpFilter:
  private val REPOSITORY_MATCHER_REGEX = Pattern.compile("^/git/([^/]+)/([^/]+?)(?:\\.git)?(/.*)?$")

  override def doFilter(
      request: HttpServletRequest,
      response: HttpServletResponse,
      chain: FilterChain
  ): Unit =
    logger.debug("Repository Filter")
    val isUpdating = request.getAttribute(InternalConstants.UPDATING_REPOSITORY_KEY).toString.toBoolean

    if !isRepositoryRequest(request)
    then rejectRequest(response, RequestRejectionExceptionType.INVALID_REQUEST)
    else
      val (username, repositoryName) = getRepositoryInfoFromUrl(request)
      logger.debug(f"Request for $username/$repositoryName")

      val repository = getRepository(username, repositoryName)
      val query = repository.flatMap {
        case Some(repo) =>
          if isUpdating || repo.isPrivate then
            val user = Option(request.getAttribute(InternalConstants.USER_KEY).asInstanceOf[NovaUser])
            if user.isEmpty then
              DBIO.failed(RequestRejectionExceptionType.INSUFFICIENT_PERMISSION.toException)
            else if user.get.user_id == repo.owner_id then DBIO.successful(Some(repo))
            else
              val accessPolicy = getAccessPolicy(user.get.user_id, repo.repository_id.get)
              accessPolicy
                .map {
                  case Some(policy) =>
                    if (isUpdating && policy.access_level == RepositoryAccessLevelType.WRITE.toString) || (!isUpdating && policy.access_level == RepositoryAccessLevelType.READ.toString)
                    then Some(repo)
                    else None
                  case None => None
                }
                .flatMap {
                  case Some(_) => DBIO.successful(Some(repo))
                  case None =>
                    if repo.isPrivate then
                      DBIO.failed(RequestRejectionExceptionType.INVALID_REPOSITORY.toException)
                    else DBIO.failed(RequestRejectionExceptionType.INSUFFICIENT_PERMISSION.toException)
                }
          else DBIO.successful(Some(repo))
        case None => DBIO.failed(RequestRejectionExceptionType.INVALID_REPOSITORY.toException)
      }

      val repositoryFuture = Database().run(query.transactionally).recoverWith { case e: Throwable =>
        Future.failed(e)
      }
      repositoryFuture.onComplete {
        case Success(repo) =>
          if repo.isEmpty then rejectRequest(response, RequestRejectionExceptionType.INVALID_REPOSITORY)
          else
            request.setAttribute(InternalConstants.REPOSITORY_KEY, repo.get)
            if isUpdating then
              request.setAttribute(
                InternalConstants.LOCKED_REPOSITORY_KEY,
                s"${repo.get.owner_id}/${repo.get.repository_id}"
              )
            logger.debug(f"Found repository with ID: ${repo.get.repository_id.get}")
            chain.doFilter(request, response)

        case Failure(e) =>
          e match
            case e: RequestRejectionException => rejectRequest(response, e.exceptionType)
            case _ => rejectRequest(response, RequestRejectionExceptionType.INVALID_REQUEST)
      }

  private def getRepositoryInfoFromUrl(req: HttpServletRequest): (String, String) =
    val matcher = REPOSITORY_MATCHER_REGEX.matcher(req.getRequestURI)
    matcher.matches()
    (matcher.group(1), matcher.group(2))

  private def isRepositoryRequest(req: HttpServletRequest): Boolean =
    REPOSITORY_MATCHER_REGEX.matcher(HttpUtil.getStrippedUrl(req)).matches()

  private def rejectRequest(
      res: HttpServletResponse,
      requestRejectionExceptionType: RequestRejectionExceptionType
  ): Unit =
    res.sendError(requestRejectionExceptionType.statusCode, requestRejectionExceptionType.message)
    asyncContext.complete()
