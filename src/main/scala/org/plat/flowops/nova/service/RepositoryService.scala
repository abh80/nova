package org.plat.flowops.nova.service

import org.plat.flowops.nova.database.schema.*
import org.plat.flowops.nova.exception.RequestRejectionExceptionType
import org.plat.flowops.nova.helper.RepositoryAccessPolicyFactory
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RepositoryService extends AuthenticationService with RepositoryAccessPolicyFactory:
  def getRepository(
      username: String,
      repositoryName: String
  ): DBIO[Option[NovaRepository]] =
    val repositoryTable = TableQuery[NovaRepositoryTable]

    if username.isEmpty || repositoryName.isEmpty then
      return DBIO.failed(RequestRejectionExceptionType.INVALID_REQUEST.toException)

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

    query

  private def findRepositoryByUserIdAndRepositoryName(
      ownerId: Long,
      repositoryName: String
  ): DBIO[Option[NovaRepository]] =

    val repositoryTable = TableQuery[NovaRepositoryTable]
    repositoryTable
      .filter(repo => repo.owner_id === ownerId && repo.repository_name === repositoryName)
      .result
      .headOption

  def getAccessPolicy(
      userId: Long,
      repositoryId: Long
  ): DBIO[Option[NovaRepositoryAccessPolicy]] =
    TableQuery[NovaRepositoryAccessPolicyTable]
      .filter(p => (p.user_id === userId).&&(p.repository_id === repositoryId))
      .result
      .headOption
