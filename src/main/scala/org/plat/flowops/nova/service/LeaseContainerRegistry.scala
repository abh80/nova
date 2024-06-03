package org.plat.flowops.nova.service

import org.plat.flowops.nova.utils.TBaseRegistry
import org.springframework.vault.core.lease.SecretLeaseContainer

import scala.collection.mutable

object LeaseContainerRegistry extends TBaseRegistry[String, SecretLeaseContainer] {
  private val registry: mutable.Map[String, SecretLeaseContainer] = mutable.Map()

  override def register(key: String, leaseContainer: SecretLeaseContainer): Unit = registry.put(key, leaseContainer)

  override def get(key: String): Option[SecretLeaseContainer] = registry.get(key)

  override def remove(key: String): Option[SecretLeaseContainer] = registry.remove(key)

  override def getAllKeys: Iterable[String] = registry.keys
}
