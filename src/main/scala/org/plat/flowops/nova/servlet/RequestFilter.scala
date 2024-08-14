package org.plat.flowops.nova.servlet

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.utils.HttpFilter

import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import org.plat.flowops.nova.utils.{ HttpRequestUtil, HttpUtil }

class RequestFilter extends HttpFilter with LazyLogging:

  private val API_BACKEND_REGEX = Pattern.compile(
    "(?x)^/repo/(.*?/((HEAD|info/refs|objects/(info/[^/]+|[a-f0-9]{2}/[a-f0-9]{38}|pack/pack-[a-f0-9]{40}\\.(pack|idx))|git-(upload|receive)-pack)))$"
  )

  private def isGitRequest(req: HttpServletRequest): Boolean =
    API_BACKEND_REGEX.matcher(HttpUtil.getStrippedUrl(req)).matches()

  override def doFilter(
      request: HttpServletRequest,
      response: HttpServletResponse,
      chain: FilterChain
  ): Unit =
    logger.whenDebugEnabled {
      logger.debug(s"Request Received: ${HttpRequestUtil.getDebugInfo(request)}")
    }

    if isGitRequest(request) then chain.doFilter(request, response)
    else response.sendError(HttpServletResponse.SC_NOT_FOUND)
