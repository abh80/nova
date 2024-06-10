package org.plat.flowops.nova.tasks.web.tasks

import org.plat.flowops.nova.tasks.Task
import org.plat.flowops.nova.tasks.web.ServerSetup

class ServerStartTask extends Task {
  override def execute(): Unit = new Thread(this).start()

  override def run(): Unit = ServerSetup.setupAndStart()
}
