package org.plat.flowops.nova.tasks.injector

import com.google.inject.Guice
import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.database.DatabaseModule
import org.plat.flowops.nova.tasks.Task

object InitializeInjector extends Task with LazyLogging:
  override def execute(): Unit =
    logger.debug("Initializing Injector...")
    InjectorHandler.initialize()

  override def run(): Unit = ()
