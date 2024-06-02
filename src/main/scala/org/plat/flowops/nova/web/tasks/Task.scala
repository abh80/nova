package org.plat.flowops.nova.web.tasks

trait Task extends Runnable {
  def execute(): Unit
}
