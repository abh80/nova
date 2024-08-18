package org.plat.flowops.nova.servlet

import org.plat.flowops.nova.constants.{ GitAllowedAuthorizationType, InternalConstants }
import org.plat.flowops.nova.database.schema.NovaUser
import org.plat.flowops.nova.exception.RequestRejectionExceptionType
import org.plat.flowops.nova.service.AuthenticationService
import org.plat.flowops.nova.utils.HttpFilter

import java.util.Base64
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import javax.servlet.{ AsyncContext, FilterChain }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

class AuthenticationFilter extends AuthenticationService with HttpFilter:

  override def doFilter(
      request: HttpServletRequest,
      response: HttpServletResponse,
      chain: FilterChain
  ): Unit =
    logger.debug("Authentication Filter")
    val isUpdating = request.getRequestURI.endsWith(
      "/git-receive-pack"
    ) || request.getQueryString == "service=git-receive-pack"
    request.setAttribute(InternalConstants.UPDATING_REPOSITORY_KEY, isUpdating)
    logger.debug(s"User wants to update the repository? $isUpdating")

    val hasAuthenticationHeader = Option(request.getHeader("Authorization")).isDefined
    if hasAuthenticationHeader then
      val f = authenticateFromHeader(request.getHeader("Authorization"))
      f.onComplete {
        case Success(user) =>
          assert(user.isDefined)
          request.setAttribute(InternalConstants.USER_KEY, user.get)
          logger.debug(s"Authenticated user: ${user.get.username}")
          chain.doFilter(request, response)

        case Failure(_) => rejectRequest(response, asyncContext, 401)
      }
    else if isUpdating then rejectRequest(response, asyncContext, 401)
    else chain.doFilter(request, response)

  private def authenticateFromHeader(authHeader: String): Future[Option[NovaUser]] =
    try
      val (authType, authData) = decodeAuthenticationHeader(authHeader)
      authType match
        case GitAllowedAuthorizationType.TOKEN =>
          authenticateByAccessKey(authData)

        case _ => Future.failed(RequestRejectionExceptionType.INVALID_TOKEN.toException)
    catch
      case e: Exception =>
        logger.error("error", e)
        Future.failed(RequestRejectionExceptionType.INVALID_TOKEN.toException)

  private def decodeAuthenticationHeader(authHeader: String): (GitAllowedAuthorizationType, String) =
    if !authHeader.toLowerCase.startsWith("basic ") then return (GitAllowedAuthorizationType.NONE, "")

    val decoded = new String(Base64.getDecoder.decode(authHeader.substring(6)))

    if decoded.split(":").length < 1 then return (null, "")

    val authString = decoded.split(":")(0)
    val allowed =
      if authString.startsWith("flat-") then GitAllowedAuthorizationType.TOKEN
      else GitAllowedAuthorizationType.NONE
    logger.debug("Auth Type: " + allowed + " Auth Data: " + authString)
    (allowed, authString)

  private def rejectRequest(
      response: HttpServletResponse,
      asyncContext: AsyncContext,
      statusCode: Int = 401
  ): Unit =
    response.setHeader("WWW-Authenticate", "Basic realm=\"Nova\"")
    response.sendError(statusCode)
    asyncContext.complete()
