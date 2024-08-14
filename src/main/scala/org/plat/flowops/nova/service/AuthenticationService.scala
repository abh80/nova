package org.plat.flowops.nova.service

import org.plat.flowops.nova.database.schema.{ NovaUser, NovaUserCreds, NovaUserCredsTable, NovaUserTable }
import org.plat.flowops.nova.exception.AuthenticationFailedExceptionType
import slick.jdbc.PostgresProfile.api.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AuthenticationService extends SystemService:
  private val userQuery = (user_id: Long) =>
    TableQuery[NovaUserTable].filter(_.user_id === user_id).result.headOption

  def authenticateByAccessKey(key: String): Future[Option[NovaUser]] =
    handleAuthentication(key)

  private def handleAuthentication(key: String): Future[Option[NovaUser]] =
    if key.isEmpty then Future.failed(AuthenticationFailedExceptionType.INVALID_TOKEN.toException)
    else getUserFromCredKey(key)

  private def getUserFromCredKey(key: String): Future[Option[NovaUser]] =
    val query = for
      creds <- TableQuery[NovaUserCredsTable].filter(_.key === key).result.headOption
      user <- creds match
        case Some(cred) => userQuery(cred.user_id)
        case None       => DBIO.successful(None)
    yield user

    Database().run(query.transactionally)
