package org.plat.flowops.nova.utils

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.database.PostgresManager

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import javax.servlet.{ Filter, FilterChain, ServletRequest, ServletResponse }

trait HttpFilter extends Filter with LazyLogging:
  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit =
    doFilter(request.asInstanceOf[HttpServletRequest], response.asInstanceOf[HttpServletResponse], chain)

  def doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain): Unit
