package org.plat.flowops.nova.fixtures

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.database.MyPostgresProfile.MyAPI.*
import org.plat.flowops.nova.database.PostgresManager
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future, Promise }
import scala.util.{ Failure, Success }

trait Fixture extends LazyLogging:
  var Database: PostgresManager = _
  def table(): TableQuery[?]

  def setDatabase(db: PostgresManager): Fixture =
    Database = db
    this

  def generate()(implicit ec: ExecutionContext): Future[?]

  def generateFixture()(implicit ec: ExecutionContext): Future[Unit] =
    val promise = Promise[Unit]()
    fixtureAlreadyGenerated().onComplete {
      case Success(true) =>
        logger.debug("Skipping fixture generation, already exists")
        promise.success(())

      case Success(false) =>
        generate().onComplete {
          case Success(_) =>
            promise.success(())

          case Failure(e) =>
            logger.error("Failed to generate fixture", e)
            promise.failure(e)
        }

      case Failure(e) =>
        logger.error("Failed to check if fixture already generated", e)
        promise.failure(e)
    }

    promise.future

  private def fixtureAlreadyGenerated(): Future[Boolean] =
    Database().run(table().length.result).map(_ > 0)

  private def handleFuture(f: Future[?])(implicit ec: ExecutionContext): Unit =
    f.onComplete {
      case scala.util.Success(_) => logger.debug("Fixture generated successfully")
      case scala.util.Failure(e) => logger.error("Fixture generation failed", e)
    }
