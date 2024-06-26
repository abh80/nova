package org.plat.flowops.nova.listeners

import com.typesafe.scalalogging.LazyLogging
import org.plat.flowops.nova.listeners.actionables.CustomLeaseActionable
import org.plat.flowops.nova.service.LeaseContainerRegistry
import org.springframework.vault.core.lease.domain.RequestedSecret
import org.springframework.vault.core.lease.event.*

class CustomLeaseListener(
    leaseContainerId: String,
    requestedSecret: RequestedSecret,
    action: CustomLeaseActionable
) extends LeaseListenerAdapter
    with LazyLogging:

  override def onLeaseEvent(leaseEvent: SecretLeaseEvent): Unit =
    if requestedSecret == leaseEvent.getSource then
      leaseEvent match
        case _: SecretLeaseCreatedEvent =>
          logger.debug("Lease created for " + leaseEvent.getSource.getPath)
          action.onLeaseCreated(
            leaseEvent
              .asInstanceOf[SecretLeaseCreatedEvent]
              .getSecrets
              .asInstanceOf[java.util.Map[String, String]]
          )

        case _: SecretLeaseExpiredEvent =>
          logger.debug("Lease expired for " + leaseEvent.getSource.getPath)
          action.onLeaseExpired(LeaseContainerRegistry.get(leaseContainerId).get, requestedSecret)
