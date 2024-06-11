package org.plat.flowops.nova.tasks.vault.tasks

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.listeners.CustomLeaseListener
import org.plat.flowops.nova.listeners.actionables.PostgresDatasourceLeaseListener
import org.plat.flowops.nova.service.LeaseContainerService.{
  addListenerToLeaseContainer,
  getLeaseContainer,
  initAndStartLeaseContainer,
  subscribeLeaseContainer
}
import org.plat.flowops.nova.service.VaultService
import org.plat.flowops.nova.tasks.Task

class PostgresDatasourceLeaseStartTask extends Task with LazyLogging:

  override def execute(): Unit =
    val DatabasePasswordPath = "database/static-creds/app-nova"
    val container            = getLeaseContainer(VaultService.getTemplate)
    val secret               = subscribeLeaseContainer(DatabasePasswordPath, container)
    addListenerToLeaseContainer(
      container,
      new CustomLeaseListener(secret, new PostgresDatasourceLeaseListener)
    )

    initAndStartLeaseContainer(container)

    logger.info("Added listener for Postgres Datasource")

  override def run(): Unit = ()
