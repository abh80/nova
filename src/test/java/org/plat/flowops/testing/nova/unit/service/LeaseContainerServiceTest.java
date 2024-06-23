package org.plat.flowops.testing.nova.unit.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.plat.flowops.nova.service.LeaseContainerService;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.lease.SecretLeaseContainer;
import org.springframework.vault.core.lease.domain.RequestedSecret;
import org.springframework.vault.core.lease.event.LeaseListener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/*
    For Classes defined in Java cannot be mocked in scala, so we need to write the test cases in Java
*/

public class LeaseContainerServiceTest {

    @Test
    void testGetLeaseContainer() {
        VaultTemplate vaultTemplate = Mockito.mock(VaultTemplate.class);
        SecretLeaseContainer leaseContainer = LeaseContainerService.getLeaseContainer(vaultTemplate);
        assertNotNull(leaseContainer);
    }

    @Test
    void testSubscribeLeaseContainer() {
        SecretLeaseContainer leaseContainer = Mockito.mock(SecretLeaseContainer.class);
        String secretPath = "testPath";
        RequestedSecret requestedSecret = LeaseContainerService.subscribeLeaseContainer(secretPath, leaseContainer);
        assertEquals(secretPath, requestedSecret.getPath());
    }

    @Test
    void testAddListenerToLeaseContainer() {
        SecretLeaseContainer leaseContainer = Mockito.mock(SecretLeaseContainer.class);
        LeaseListener leaseListener = Mockito.mock(LeaseListener.class);
        SecretLeaseContainer result = LeaseContainerService.addListenerToLeaseContainer(leaseContainer, leaseListener);
        assertEquals(leaseContainer, result);
    }

    @Test
    void testInitAndStartLeaseContainer() {
        SecretLeaseContainer leaseContainer = Mockito.mock(SecretLeaseContainer.class);
        LeaseContainerService.initAndStartLeaseContainer(leaseContainer);
        Mockito.verify(leaseContainer).afterPropertiesSet();
        Mockito.verify(leaseContainer).start();
    }
}