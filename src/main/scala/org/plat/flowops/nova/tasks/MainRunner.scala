package org.plat.flowops.nova.tasks

import org.plat.flowops.nova.tasks.injector.InitializeInjector
import org.plat.flowops.nova.tasks.scheduler.Scheduler
import org.plat.flowops.nova.tasks.vault.VaultInitializer
import org.plat.flowops.nova.tasks.web.ServletMainRunner

object MainRunner extends App:
  InitializeInjector.execute()
  Scheduler.execute()
  ServletMainRunner.execute()
  VaultInitializer.execute()
