package org.plat.flowops.nova.servlet

import org.plat.flowops.nova.constants.InternalConstants
import org.plat.flowops.nova.exception.RequestRejectionExceptionType
import org.plat.flowops.nova.service.RepositoryService
import org.plat.flowops.nova.utils.{ HttpFilter, HttpUtil }

import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import scala.concurrent.ExecutionContext.Implicits.global
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

      val repositoryFuture = getRepository(username, repositoryName)

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
            logger.debug(f"Found repository with ID: ${repo.get.repository_id}")
            chain.doFilter(request, response)

        case Failure(_) => rejectRequest(response, RequestRejectionExceptionType.INVALID_REPOSITORY)
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
