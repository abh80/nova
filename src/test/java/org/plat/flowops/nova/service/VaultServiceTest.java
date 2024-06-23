package org.plat.flowops.nova.service;

import org.junit.jupiter.api.Test;
import org.springframework.vault.core.VaultTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VaultServiceTest {

    @Test
    public void shouldInitializeConnectionIfNotAlreadyInitialized() {
        VaultTemplate mockVaultTemplate = mock(VaultTemplate.class);

        VaultService.initConnection("http://localhost:8200", "token");
        VaultTemplate vaultTemplate = VaultService.getTemplate();

        assertNotNull(vaultTemplate);
    }

    @Test
    public void shouldNotReinitializeConnectionIfAlreadyInitialized() {
        VaultTemplate mockVaultTemplate = mock(VaultTemplate.class);

        VaultService.initConnection("http://localhost:8200", "token");
        VaultTemplate vaultTemplate1 = VaultService.getTemplate();

        VaultService.initConnection("http://localhost:8200", "token");
        VaultTemplate vaultTemplate2 = VaultService.getTemplate();

        assertSame(vaultTemplate1, vaultTemplate2);
    }

    @Test
    public void shouldReturnTheInitializedVaultTemplate() {
        VaultTemplate mockVaultTemplate = mock(VaultTemplate.class);

        VaultService.initConnection("http://localhost:8200", "token");
        VaultTemplate vaultTemplate = VaultService.getTemplate();

        assertNotNull(vaultTemplate);
    }
}