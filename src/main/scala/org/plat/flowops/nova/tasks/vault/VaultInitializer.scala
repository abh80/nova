package org.plat.flowops.nova.tasks.vault

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.service.VaultService
import org.plat.flowops.nova.tasks.Task
import org.plat.flowops.nova.tasks.vault.tasks.PostgresDatasourceLeaseStartTask
import org.plat.flowops.nova.utils.EnvironmentLoader

object VaultInitializer extends LazyLogging with Task:

  override def execute(): Unit =
    logger.info("Initializing Vault Template...")
    val VAULT_ADDR  = EnvironmentLoader.getRequiredEnvironmentVariable("VAULT_ADDR")
    val VAULT_TOKEN = EnvironmentLoader.getRequiredEnvironmentVariable("VAULT_APP_TOKEN")

    VaultService.initConnection(VAULT_ADDR, VAULT_TOKEN)

    new PostgresDatasourceLeaseStartTask().execute()

  override def run(): Unit = ()
