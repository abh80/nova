package org.plat.flowops.nova.listeners.actionables

import org.springframework.vault.core.lease.SecretLeaseContainer
import org.springframework.vault.core.lease.domain.RequestedSecret

trait CustomLeaseActionable:
  def onLeaseExpired(secretLeaseContainer: SecretLeaseContainer, requestedSecret: RequestedSecret): Unit

  def onLeaseCreated(secrets: java.util.Map[String, String]): Unit
