package org.plat.flowops.nova.fixtures
import org.plat.flowops.nova.constants.RepositoryAccessLevelType.WRITE
import org.plat.flowops.nova.database.MyPostgresProfile.MyAPI.*
import org.plat.flowops.nova.database.schema.NovaRepositoryAccessPolicyTable
import org.plat.flowops.nova.helper.RepositoryAccessPolicyFactory
import slick.lifted.TableQuery

import scala.concurrent.{ ExecutionContext, Future }

class MockRepositoryPolicy extends Fixture:

  private val mockUsers        = List(1L, 2L)
  private val mockRepositories = List(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)

  override def generate()(implicit ec: ExecutionContext): Future[?] =
    val policyTable = table()
    val query = for
      user <- mockUsers
      repo <- mockRepositories
    yield policyTable += RepositoryAccessPolicyFactory.generatePolicy(user, repo, WRITE)

    Database().run(DBIO.seq(query*))

  override def table(): TableQuery[NovaRepositoryAccessPolicyTable] =
    TableQuery[NovaRepositoryAccessPolicyTable]
