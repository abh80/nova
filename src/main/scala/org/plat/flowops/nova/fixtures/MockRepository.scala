package org.plat.flowops.nova.fixtures
import org.plat.flowops.nova.database.MyPostgresProfile.MyAPI.queryInsertActionExtensionMethods
import org.plat.flowops.nova.database.schema.{ NovaRepository, NovaRepositoryTable }
import slick.lifted.TableQuery

import java.sql.Timestamp
import scala.concurrent.{ ExecutionContext, Future }

class MockRepository extends Fixture:

  private val time = System.currentTimeMillis()

  private val mockRepositories = List(
    NovaRepository(
      1L,
      "repo1",
      None,
      new Timestamp(time),
      new Timestamp(time),
      isPrivate = false,
      "master",
      "description"
    ),
    NovaRepository(
      1L,
      "repo2",
      None,
      new Timestamp(time),
      new Timestamp(time),
      isPrivate = false,
      "master",
      "description"
    ),
    NovaRepository(
      1L,
      "repo3",
      None,
      new Timestamp(time),
      new Timestamp(time),
      isPrivate = false,
      "master",
      "description"
    ),
    NovaRepository(
      1L,
      "repo4",
      None,
      new Timestamp(time),
      new Timestamp(time),
      isPrivate = false,
      "master",
      "description"
    ),
    NovaRepository(
      2L,
      "repo5",
      None,
      new Timestamp(time),
      new Timestamp(time),
      isPrivate = false,
      "master",
      "description"
    ),
    NovaRepository(
      2L,
      "repo6",
      None,
      new Timestamp(time),
      new Timestamp(time),
      isPrivate = false,
      "master",
      "description"
    ),
    NovaRepository(
      2L,
      "repo7",
      None,
      new Timestamp(time),
      new Timestamp(time),
      isPrivate = false,
      "master",
      "description"
    ),
    NovaRepository(
      2L,
      "repo8",
      None,
      new Timestamp(time),
      new Timestamp(time),
      isPrivate = false,
      "master",
      "description"
    ),
    NovaRepository(
      2L,
      "repo9",
      None,
      new Timestamp(time),
      new Timestamp(time),
      isPrivate = false,
      "master",
      "description"
    ),
    NovaRepository(
      1L,
      "repo10",
      None,
      new Timestamp(time),
      new Timestamp(time),
      isPrivate = false,
      "master",
      "description"
    )
  )

  override def generate()(implicit ec: ExecutionContext): Future[?] =
    val repositories = table()
    Database().run(repositories ++= mockRepositories)

  override def table(): TableQuery[NovaRepositoryTable] = TableQuery[NovaRepositoryTable]
