package org.plat.flowops.nova.web

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.listeners.CustomLeaseListener
import org.plat.flowops.nova.listeners.actionables.PostgresDatasourceLeaseListener
import org.plat.flowops.nova.service.LeaseContainerService.{getLeaseContainer, subscribeLeaseContainer, addListenerToLeaseContainer}
import org.plat.flowops.nova.service.VaultService
import org.plat.flowops.nova.utils.EnvironmentLoader
import org.plat.flowops.nova.web.tasks.{ServerStartTask, Task}

object ServletMainRunner extends App with LazyLogging {

  logger.info("Starting Servlet Main Runner")

  private val startTask: Task = new ServerStartTask()
  startTask.execute()

  VaultService.initConnection(EnvironmentLoader.getRequiredEnvironmentVariable("VAULT_ADDR"), EnvironmentLoader.getRequiredEnvironmentVariable("VAULT_APP_TOKEN"))

  private val container = getLeaseContainer(VaultService.getTemplate)
  private val secret = subscribeLeaseContainer("database/static-creds/app-nova", container)

  addListenerToLeaseContainer(container, new CustomLeaseListener(secret, new PostgresDatasourceLeaseListener))
  container.afterPropertiesSet()
  container.start()
  logger.info("Added listener")
}