package org.plat.flowops.nova.tasks

import org.plat.flowops.nova.tasks.vault.VaultInitializer
import org.plat.flowops.nova.tasks.web.ServletMainRunner

object MainRunner extends App:
  ServletMainRunner.execute()
  VaultInitializer.execute()
