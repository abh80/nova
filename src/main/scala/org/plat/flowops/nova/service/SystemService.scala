package org.plat.flowops.nova.service

import org.plat.flowops.nova.constants.DefaultEnvironmentConstants
import org.plat.flowops.nova.database.PostgresManager
import org.plat.flowops.nova.utils.EnvironmentLoader

trait SystemService:
  val DIRECTORY: Directory.type           = Directory
  protected var Database: PostgresManager = PostgresManager()

object Directory:
  val NOVA_HOME: String =
    EnvironmentLoader.getEnvironmentVariable("BASE_PATH", DefaultEnvironmentConstants.BASE_PATH)
  val TMP_DIR: String  = NOVA_HOME + "/tmp"
  val REPO_DIR: String = NOVA_HOME + "/repositories"

  def createDirectories(): Unit =
    List(NOVA_HOME, TMP_DIR, REPO_DIR).foreach { dir =>
      val file = new java.io.File(dir)
      if !file.exists() then file.mkdirs()
    }
