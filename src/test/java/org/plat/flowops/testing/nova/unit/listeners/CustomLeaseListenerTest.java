package org.plat.flowops.testing.nova.unit.listeners;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.plat.flowops.nova.listeners.CustomLeaseListener;
import org.plat.flowops.nova.listeners.actionables.CustomLeaseActionable;
import org.plat.flowops.nova.service.LeaseContainerRegistry;
import org.springframework.vault.core.lease.SecretLeaseContainer;
import org.springframework.vault.core.lease.domain.RequestedSecret;
import org.springframework.vault.core.lease.event.SecretLeaseCreatedEvent;
import org.springframework.vault.core.lease.event.SecretLeaseEvent;
import org.springframework.vault.core.lease.event.SecretLeaseExpiredEvent;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomLeaseListenerTest {
    
    private final String containerID = "test-container";

    @Test
    public void testOnLeaseEventCreated() {
        CustomLeaseActionable action = mock(CustomLeaseActionable.class);
        RequestedSecret requestedSecret = mock(RequestedSecret.class);
        SecretLeaseCreatedEvent leaseEvent = mock(SecretLeaseCreatedEvent.class);

        when(leaseEvent.getSource()).thenReturn(requestedSecret);
        when(leaseEvent.getSecrets()).thenReturn(new HashMap<>());

        CustomLeaseListener listener = new CustomLeaseListener("testId", requestedSecret, action);
        listener.onLeaseEvent(leaseEvent);

        verify(action).onLeaseCreated(any());
    }

    @Test
    public void testOnLeaseEventExpired() {
        CustomLeaseActionable action = mock(CustomLeaseActionable.class);
        RequestedSecret requestedSecret = mock(RequestedSecret.class);
        SecretLeaseExpiredEvent leaseEvent = mock(SecretLeaseExpiredEvent.class);
        SecretLeaseContainer container = mock(SecretLeaseContainer.class);

        LeaseContainerRegistry.register(containerID, container);

        Assertions.assertEquals(container, LeaseContainerRegistry.get(containerID).get());
        
        when(leaseEvent.getSource()).thenReturn(requestedSecret);

        CustomLeaseListener listener = new CustomLeaseListener(containerID, requestedSecret, action);
        listener.onLeaseEvent(leaseEvent);

        verify(action).onLeaseExpired(any(), eq(requestedSecret));
    }
}