package org.plat.flowops.nova.database

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import org.plat.flowops.nova.listeners.{EventListenerAdapter, PostgresDatabaseEventListener}

class DatabaseModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    val eventListener = new PostgresDatabaseEventListener
    bind[EventListenerAdapter].toInstance(eventListener)
    bind[PostgresManager].toInstance(PostgresManager(eventListener))
  }
}
