package org.plat.flowops.nova.exception

class AuthenticationFailedException(message: AuthenticationFailedExceptionType)
    extends Exception(message.toString) {}

enum AuthenticationFailedExceptionType:
  case INVALID_CREDENTIALS, INVALID_TOKEN

  val toException: AuthenticationFailedException = AuthenticationFailedException(this)
