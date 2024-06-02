package org.plat.flowops.nova.web.tasks

import org.plat.flowops.nova.web.ServerSetup

class ServerStartTask extends Task {
  override def execute(): Unit = new Thread(this).start()

  override def run(): Unit = ServerSetup.setupAndStart()
}
