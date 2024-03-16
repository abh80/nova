package org.plat.flowops.nova.utils

import javax.servlet.http.HttpServletRequest

object HttpRequestUtil {
  def getDebugInfo(request: HttpServletRequest): String = {
    val sb = new StringBuilder
    sb.append("Request URL: ").append(request.getRequestURL).append("\n")
    sb.append("Request URI: ").append(request.getRequestURI).append("\n")
    sb.append("Request Method: ").append(request.getMethod).append("\n")
    sb.append("Request Query String: ").append(request.getQueryString).append("\n")
    sb.append("Request Remote Addr: ").append(request.getRemoteAddr).append("\n")
    sb.append("Request Remote Host: ").append(request.getRemoteHost).append("\n")
    sb.append("Request Remote Port: ").append(request.getRemotePort).append("\n")
    sb.append("Request Local Addr: ").append(request.getLocalAddr).append("\n")
    sb.append("Request Local Name: ").append(request.getLocalName).append("\n")
    sb.append("Request Local Port: ").append(request.getLocalPort).append("\n")
    sb.append("Request Server Name: ").append(request.getServerName).append("\n")
    sb.append("Request Server Port: ").append(request.getServerPort).append("\n")
    sb.append("Request Scheme: ").append(request.getScheme).append("\n")
    sb.append("Request Protocol: ").append(request.getProtocol).append("\n")
    sb.append("Request Context Path: ").append(request.getContextPath).append("\n")
    sb.append("Request Servlet Path: ").append(request.getServletPath).append("\n")
    sb.append("Request Path Info: ").append(request.getPathInfo).append("\n")
    sb.append("Request Path Translated: ").append(request.getPathTranslated).append("\n")
    sb.append("Request Auth Type: ").append(request.getAuthType).append("\n")
    sb.append("Request Content Type: ").append(request.getContentType).append("\n")
    sb.append("Request Content Length: ").append(request.getContentLength).append("\n")
    sb.append("Request Character Encoding: ").append(request.getCharacterEncoding).append("\n")
    sb.append("Request Locale: ").append(request.getLocale).append("\n")
    sb.append("Request Locales: ").append(request.getLocales).append("\n")
    sb.append("Request Headers: ").append("\n")
    val headerNames = request.getHeaderNames
    while (headerNames.hasMoreElements) {
      val headerName = headerNames.nextElement
      sb.append("  ").append(headerName).append(": ").append(request.getHeader(headerName)).append("\n")
    }
    sb.toString
  }
}
