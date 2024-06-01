package org.plat.flowops.nova.web.tasks

import org.plat.flowops.nova.web.ServerSetup

class ServerStartTask extends Task {
  override def execute(): Unit = ServerSetup.setupAndStart()
}
