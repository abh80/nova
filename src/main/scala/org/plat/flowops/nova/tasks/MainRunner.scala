package org.plat.flowops.nova.tasks

import org.plat.flowops.nova.constants.DefaultEnvironmentConstants.DISABLE_VAULT
import org.plat.flowops.nova.service.Directory
import org.plat.flowops.nova.tasks.database.InitializeDatabase
import org.plat.flowops.nova.tasks.injector.InitializeInjector
import org.plat.flowops.nova.tasks.scheduler.Scheduler
import org.plat.flowops.nova.tasks.vault.VaultInitializer
import org.plat.flowops.nova.tasks.web.ServletMainRunner
import org.plat.flowops.nova.utils.EnvironmentLoader

object MainRunner extends App:
  Directory.createDirectories()
  private val disableVaultFlag =
    EnvironmentLoader.getEnvironmentVariable("DISABLE_VAULT", DISABLE_VAULT).toBoolean

  InitializeInjector.execute()
  Scheduler.execute()
  ServletMainRunner.execute()

  if disableVaultFlag then InitializeDatabase.execute()
  else VaultInitializer.execute()
