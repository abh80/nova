package org.plat.flowops.nova.fixtures

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.database.PostgresManager

import scala.concurrent.{ ExecutionContext, Future }

trait Fixture extends LazyLogging:
  var Database: PostgresManager = _
  def setDatabase(db: PostgresManager): Fixture =
    Database = db
    this

  def generate()(implicit ec: ExecutionContext): Future[?]

  def generateFixture()(implicit ec: ExecutionContext): Unit =
    handleFuture(generate())

  private def handleFuture(f: Future[?])(implicit ec: ExecutionContext): Unit =
    f.onComplete {
      case scala.util.Success(_) => logger.debug("Fixture generated successfully")
      case scala.util.Failure(e) => logger.error("Fixture generation failed", e)
    }
