package org.plat.flowops.nova.service

import org.plat.flowops.nova.database.schema.{ NovaRepository, NovaRepositoryTable, NovaUser, NovaUserTable }
import org.plat.flowops.nova.exception.RequestRejectionExceptionType
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RepositoryService extends AuthenticationService:
  def getRepository(
      username: String,
      repositoryName: String,
      user: Option[NovaUser] = None,
      isUpdating: Boolean = false
  ): Future[Option[NovaRepository]] =
    val repositoryTable = TableQuery[NovaRepositoryTable]
    if username.isEmpty || repositoryName.isEmpty then
      return Future.failed(RequestRejectionExceptionType.INVALID_REQUEST.toException)

    val checkPermission = isUpdating && user.isDefined && user.get.username == username.toLowerCase

    val query = for
      userOpt <- TableQuery[NovaUserTable]
        .filter(_.username === username.toLowerCase)
        .map(_.user_id)
        .result
        .headOption
      repository <- userOpt match
        case Some(user) => findRepositoryByUserIdAndRepositoryName(user, repositoryName.toLowerCase)
        case None       => DBIO.failed(RequestRejectionExceptionType.INVALID_REPOSITORY.toException)
    yield repository

    Database().run(query.transactionally)

  private def findRepositoryByUserIdAndRepositoryName(
      ownerId: Long,
      repositoryName: String
  ): DBIO[Option[NovaRepository]] =

    val repositoryTable = TableQuery[NovaRepositoryTable]
    repositoryTable
      .filter(repo => repo.owner_id === ownerId && repo.repository_name === repositoryName)
      .result
      .headOption
