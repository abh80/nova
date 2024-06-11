package org.plat.flowops.nova.service

import org.springframework.vault.core.VaultTemplate
import org.springframework.vault.core.lease.SecretLeaseContainer
import org.springframework.vault.core.lease.domain.RequestedSecret
import org.springframework.vault.core.lease.event.{ LeaseListener, SecretLeaseEvent }

object LeaseContainerService:
  def getLeaseContainer(vaultTemplate: VaultTemplate): SecretLeaseContainer = new SecretLeaseContainer(
    vaultTemplate
  )

  def subscribeLeaseContainer(
      secretPath: String,
      secretLeaseContainer: SecretLeaseContainer
  ): RequestedSecret =
    val requestedSecret = RequestedSecret.from(RequestedSecret.Mode.RENEW, secretPath)
    secretLeaseContainer.addRequestedSecret(requestedSecret)
    requestedSecret

  def addListenerToLeaseContainer(
      secretLeaseContainer: SecretLeaseContainer,
      leaseListener: LeaseListener
  ): SecretLeaseContainer =
    secretLeaseContainer.addLeaseListener(leaseListener)
    secretLeaseContainer

  def initAndStartLeaseContainer(leaseContainer: SecretLeaseContainer): Unit =
    leaseContainer.afterPropertiesSet()
    leaseContainer.start()
