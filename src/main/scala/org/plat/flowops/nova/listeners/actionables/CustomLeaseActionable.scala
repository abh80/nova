package org.plat.flowops.nova.listeners.actionables

trait CustomLeaseActionable {
  def onLeaseExpired(): Unit

  def onLeaseCreated(secrets: java.util.Map[String, String]): Unit
}
