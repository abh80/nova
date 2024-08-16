package org.plat.flowops.nova.utils

import com.typesafe.scalalogging.LazyLogging

import javax.servlet.*
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

trait HttpFilter extends Filter with LazyLogging:
  protected var asyncContext: AsyncContext = _

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit =
    if !request.isAsyncStarted then asyncContext = request.startAsync()
    else asyncContext = request.getAsyncContext

    doFilter(
      request.asInstanceOf[HttpServletRequest],
      response.asInstanceOf[HttpServletResponse],
      chain
    )

  def doFilter(
      request: HttpServletRequest,
      response: HttpServletResponse,
      chain: FilterChain
  ): Unit
