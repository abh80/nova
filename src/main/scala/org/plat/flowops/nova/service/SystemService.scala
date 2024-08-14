package org.plat.flowops.nova.service

import com.google.inject.Inject
import org.plat.flowops.nova.database.PostgresManager

trait SystemService:
  @Inject
  protected var Database: PostgresManager = _
