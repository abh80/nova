package org.plat.flowops.nova.utils

import javax.servlet.http.HttpServletRequest

object HttpUtil:

  private def getStrippedUrl(request: HttpServletRequest, url: String): String =
    url.substring(request.getContextPath.length())

  def getBaseUrl(request: HttpServletRequest): String =
    request.getContextPath

  def getStrippedUrl(request: HttpServletRequest): String =
    getStrippedUrl(request, request.getRequestURI)
