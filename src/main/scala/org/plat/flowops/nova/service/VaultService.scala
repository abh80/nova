package org.plat.flowops.nova.service

import org.springframework.vault.authentication.TokenAuthentication
import org.springframework.vault.client.VaultEndpoint
import org.springframework.vault.core.VaultTemplate
import org.springframework.vault.core.lease.SecretLeaseContainer

object VaultService {
  private var vaultTemplate: VaultTemplate = _

  def initConnection(vault_server_addr: String, vault_server_host: Int, vault_app_token: String): Unit =
    if (vaultTemplate != null) return
    val endpoint = VaultEndpoint.create(vault_server_addr, vault_server_host)
    val tokenAuth = new TokenAuthentication(vault_app_token)
    vaultTemplate = new VaultTemplate(endpoint, tokenAuth)

  def getTemplate: VaultTemplate = vaultTemplate
}
