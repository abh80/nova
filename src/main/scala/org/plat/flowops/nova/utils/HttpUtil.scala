package org.plat.flowops.nova.utils

import javax.servlet.http.HttpServletRequest

object HttpUtil:

  def getBaseUrl(request: HttpServletRequest): String =
    _getBaseUrl(request)

  private def _getBaseUrl(request: HttpServletRequest): String =
    val scheme = request.getScheme

    val serverName = request.getServerName

    val serverPort = request.getServerPort

    val contextPath = request.getContextPath

    // Construct the base URL
    val url = new StringBuilder
    url.append(scheme).append("://").append(serverName)
    // If it's a non-default port, include it in the URL
    if (scheme == "http" && serverPort != 80) || (scheme == "https" && serverPort != 443) then
      url.append(":").append(serverPort)
    url.append(if contextPath == null then "" else contextPath)
    url.toString

  def getStrippedUrl(request: HttpServletRequest): String =
    getStrippedUrl(request, request.getRequestURL.toString)

  private def getStrippedUrl(request: HttpServletRequest, url: String): String =
    url.substring(_getBaseUrl(request).length)
