package org.plat.flowops.nova.fixtures
import org.plat.flowops.nova.database.MyPostgresProfile.MyAPI.queryInsertActionExtensionMethods
import org.plat.flowops.nova.database.schema.{ NovaUser, NovaUserTable }
import org.plat.flowops.nova.utils.Snowflake
import slick.lifted.TableQuery

import scala.concurrent.{ ExecutionContext, Future }

class MockUsers extends Fixture:
  private var mockUsers = List(("root", "root", 1L), ("abh80", "abh80", Snowflake().generateId()))

  override def generate()(implicit ec: ExecutionContext): Future[?] =
    logger.debug("Generating mock users fixture")
    for i <- 1 to 10 do mockUsers :+= (f"user_${i}", f"user_${i}", Snowflake().generateId())
    val users = TableQuery[NovaUserTable]
    Database().run(users ++= mockUsers.map((NovaUser.apply _).tupled))
