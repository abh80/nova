package org.plat.flowops.nova.tasks

trait Task extends Runnable:
  def execute(): Unit
