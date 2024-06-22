package org.plat.flowops.nova.tasks

import org.plat.flowops.nova.database.PostgresManager
import org.plat.flowops.nova.listeners.PostgresDatabaseEventListener
import org.plat.flowops.nova.tasks.scheduler.Scheduler
import org.plat.flowops.nova.tasks.vault.VaultInitializer
import org.plat.flowops.nova.tasks.web.ServletMainRunner

object MainRunner extends App:
  Scheduler.execute()
  PostgresManager(new PostgresDatabaseEventListener)
  ServletMainRunner.execute()
  VaultInitializer.execute()
