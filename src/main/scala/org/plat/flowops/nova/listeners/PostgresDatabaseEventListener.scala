package org.plat.flowops.nova.listeners

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.database.job.SchemaCreationJob
import org.plat.flowops.nova.helper.JobRunner
import org.plat.flowops.nova.listeners.events.*

class PostgresDatabaseEventListener extends EventListenerAdapter with LazyLogging:
  override def onEvent(event: Event): Unit =
    event.eventType match
      case _: DatabaseConnectedEvent =>
        logger.debug("Database Connected event!")
        JobRunner.runOnce(classOf[SchemaCreationJob].getName)
