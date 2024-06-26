package org.plat.flowops.nova.service

import org.springframework.vault.authentication.TokenAuthentication
import org.springframework.vault.client.VaultEndpoint
import org.springframework.vault.core.VaultTemplate
import org.springframework.vault.core.lease.SecretLeaseContainer

object VaultService:
  private var vaultTemplate: VaultTemplate = _

  def initConnection(vault_server_addr: String, vault_app_token: String): Unit =
    if vaultTemplate != null then return
    val endpoint  = VaultEndpoint.from(vault_server_addr)
    val tokenAuth = new TokenAuthentication(vault_app_token)
    vaultTemplate = new VaultTemplate(endpoint, tokenAuth)
    vaultTemplate.afterPropertiesSet()

  def getTemplate: VaultTemplate = vaultTemplate
