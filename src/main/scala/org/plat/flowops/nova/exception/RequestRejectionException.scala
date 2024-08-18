package org.plat.flowops.nova.exception

import org.eclipse.jetty.http.HttpStatus

class RequestRejectionException(val exceptionType: RequestRejectionExceptionType, message: String)
    extends Exception(message):
  def statusCode: Int = exceptionType.statusCode

sealed trait RequestRejectionExceptionType:

  def statusCode: Int
  def message: String
  def toException: RequestRejectionException = RequestRejectionException(this, message)

object RequestRejectionExceptionType:
  case object INVALID_REQUEST extends RequestRejectionExceptionType:
    val statusCode: Int = HttpStatus.BAD_REQUEST_400
    val message: String = "Invalid request"

  case object INVALID_REPOSITORY extends RequestRejectionExceptionType:
    val statusCode: Int = HttpStatus.NOT_FOUND_404
    val message: String = "Invalid repository"

  case object INVALID_BRANCH extends RequestRejectionExceptionType:
    val statusCode: Int = HttpStatus.NOT_FOUND_404
    val message: String = "Invalid branch"

  case object INVALID_TOKEN extends RequestRejectionExceptionType:
    val statusCode: Int = HttpStatus.UNAUTHORIZED_401
    val message: String = "Invalid token"

  case object INSUFFICIENT_PERMISSION extends RequestRejectionExceptionType:
    val statusCode: Int = HttpStatus.FORBIDDEN_403
    val message: String = "Insufficient permission"
