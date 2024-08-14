package org.plat.flowops.nova.database

import com.google.inject.{ AbstractModule, Singleton }
import net.codingwell.scalaguice.ScalaModule
import org.plat.flowops.nova.listeners.{ EventListenerAdapter, PostgresDatabaseEventListener }

class DatabaseModule extends AbstractModule with ScalaModule:
  override def configure(): Unit =
    bind[EventListenerAdapter].to[PostgresDatabaseEventListener]
