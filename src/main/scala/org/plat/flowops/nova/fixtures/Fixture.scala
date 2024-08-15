package org.plat.flowops.nova.fixtures

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.database.MyPostgresProfile.MyAPI.*
import org.plat.flowops.nova.database.PostgresManager
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future }

trait Fixture extends LazyLogging:
  var Database: PostgresManager = _
  def table(): TableQuery[?]

  def setDatabase(db: PostgresManager): Fixture =
    Database = db
    this

  def generate()(implicit ec: ExecutionContext): Future[?]

  def generateFixture()(implicit ec: ExecutionContext): Unit =
    fixtureAlreadyGenerated().onComplete {
      case scala.util.Success(true)  => logger.debug("Skipping fixture generation, already exists")
      case scala.util.Success(false) => handleFuture(generate())
      case scala.util.Failure(e)     => logger.error("Failed to check if fixture already generated", e)
    }

  private def handleFuture(f: Future[?])(implicit ec: ExecutionContext): Unit =
    f.onComplete {
      case scala.util.Success(_) => logger.debug("Fixture generated successfully")
      case scala.util.Failure(e) => logger.error("Fixture generation failed", e)
    }

  private def fixtureAlreadyGenerated(): Future[Boolean] =
    Database().run(table().length.result).map(_ > 0)
