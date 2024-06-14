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
import org.plat.flowops.nova.service.{ LeaseContainerRegistry, VaultService }
import org.plat.flowops.nova.tasks.Task

class PostgresDatasourceLeaseStartTask extends Task with LazyLogging:

  override def execute(): Unit =
    val DatabasePasswordPath = "database/creds/app-nova"
    val container            = getLeaseContainer(VaultService.getTemplate)
    val secret               = subscribeLeaseContainer(DatabasePasswordPath, container)
    val containerID          = "postgres-datasource-lease-container"
    LeaseContainerRegistry.register(containerID, container)
    addListenerToLeaseContainer(
      container,
      new CustomLeaseListener(containerID, secret, new PostgresDatasourceLeaseListener)
    )

    initAndStartLeaseContainer(container)

    logger.info("Added listener for Postgres Datasource")

  override def run(): Unit = ()
