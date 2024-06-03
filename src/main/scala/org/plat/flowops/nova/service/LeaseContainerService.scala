package org.plat.flowops.nova.service

import org.springframework.vault.core.VaultTemplate
import org.springframework.vault.core.lease.SecretLeaseContainer
import org.springframework.vault.core.lease.domain.RequestedSecret
import org.springframework.vault.core.lease.event.{LeaseListener, SecretLeaseEvent}
import org.springframework.vault.core.lease.SecretLeaseEventPublisher

object LeaseContainerService {
  def getLeaseContainer(vaultTemplate: VaultTemplate): SecretLeaseContainer = new SecretLeaseContainer(vaultTemplate)

  def subscribeLeaseContainer(secretPath: String, secretLeaseContainer: SecretLeaseContainer): SecretLeaseContainer =
    val requestedSecret = RequestedSecret.from(RequestedSecret.Mode.RENEW, secretPath)
    secretLeaseContainer.addRequestedSecret(requestedSecret)
    secretLeaseContainer

  def addListenerToLeaseContainer(secretLeaseContainer: SecretLeaseContainer, leaseListener: LeaseListener): SecretLeaseContainer =
    secretLeaseContainer.addLeaseListener(leaseListener)
    secretLeaseContainer
}
