package org.plat.flowops.nova.fixtures
import org.plat.flowops.nova.database.MyPostgresProfile.MyAPI.*
import org.plat.flowops.nova.database.schema.{ NovaUserCreds, NovaUserCredsTable, NovaUserTable }
import org.plat.flowops.nova.utils.AccessTokenGenerator
import slick.lifted.TableQuery

import java.sql.Timestamp
import scala.concurrent.{ ExecutionContext, Future }

class MockUserCreds extends Fixture:

  override def generate()(implicit ec: ExecutionContext): Future[?] =
    val users = TableQuery[NovaUserTable]

    logger.debug("Generating mock user creds fixture")

    val userCreds = table()

    Database()
      .run {
        users.result.flatMap { userRows =>
          DBIO.sequence {
            userRows.map { user =>
              val (access_token, access_token_id) = AccessTokenGenerator.generateAccessToken()
              userCreds += NovaUserCreds(
                user.user_id,
                access_token,
                Timestamp.from(java.time.Instant.now()),
                Timestamp.from(java.time.Instant.now().plusSeconds(365 * 24 * 60 * 60)),
                List("read", "write"),
                access_token_id
              )
            }
          }.transactionally
        }
      }
      .map(_ => ())

  override def table(): TableQuery[NovaUserCredsTable] = TableQuery[NovaUserCredsTable]
