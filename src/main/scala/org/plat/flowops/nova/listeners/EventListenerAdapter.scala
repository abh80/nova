package org.plat.flowops.nova.listeners

trait EventListenerAdapter:
  /** Called when an event is received.
    * @param event
    *   the event that was received
    */
  def onEvent(event: Event): Unit
