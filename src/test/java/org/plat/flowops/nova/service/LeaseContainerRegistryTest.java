package org.plat.flowops.nova.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.vault.core.lease.SecretLeaseContainer;

/*
    For Classes defined in Java cannot be mocked in scala, so we need to write the test cases in Java
*/

public class LeaseContainerRegistryTest {
    @Test
    void leaseContainerRegistryShouldRegisterGetRemoveAndListKeys() {
        SecretLeaseContainer leaseContainer = Mockito.mock(SecretLeaseContainer.class);
        String key = "testKey";

        LeaseContainerRegistry.register(key, leaseContainer);

        Assertions.assertEquals(leaseContainer, LeaseContainerRegistry.get(key).get());

        Assertions.assertTrue(LeaseContainerRegistry.getAllKeys().find(k -> k.equals(key)).isDefined());
        
        LeaseContainerRegistry.remove(key);
        
        Assertions.assertFalse(LeaseContainerRegistry.get(key).isDefined());
    }
}
