package org.plat.flowops.nova.tasks.injector

import com.google.inject.{Guice, Injector}
import org.plat.flowops.nova.database.DatabaseModule

object InjectorHandler:
  private var injector: Injector = _

  def initialize(): Unit =
    if injector == null then
      synchronized {
        if injector == null then injector = Guice.createInjector(new DatabaseModule)
      }

  def getInjector: Injector =
    if injector == null then
      throw new IllegalStateException("Injector not initialized. Call initialize(eventListener) first.")
    injector
