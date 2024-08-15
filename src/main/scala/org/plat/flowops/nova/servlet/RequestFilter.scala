package org.plat.flowops.nova.servlet

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.utils.{ HttpFilter, HttpRequestUtil, HttpUtil }

import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

class RequestFilter extends HttpFilter:

  private val API_BACKEND_REGEX = Pattern.compile(
    "(?x)^/(git/)?repo/(.*?/((HEAD|info/refs|objects/(info/[^/]+|[a-f0-9]{2}/[a-f0-9]{38}|pack/pack-[a-f0-9]{40}\\.(pack|idx))|git-(upload|receive)-pack)))$"
  )

  override def doFilter(
      request: HttpServletRequest,
      response: HttpServletResponse,
      chain: FilterChain
  ): Unit =
    logger.debug("Request Filter")
    logger.whenDebugEnabled {
      logger.debug(s"Request Received: ${HttpRequestUtil.getDebugInfo(request)}")
    }

    if isGitRequest(request) then chain.doFilter(request, response)
    else response.sendError(HttpServletResponse.SC_NOT_FOUND)

  private def isGitRequest(req: HttpServletRequest): Boolean =
    print(HttpUtil.getStrippedUrl(req))
    API_BACKEND_REGEX.matcher(HttpUtil.getStrippedUrl(req)).matches()
