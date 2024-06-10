package org.plat.flowops.nova.constants

enum DefaultEnvironmentConstants(val value: String) {
  case PORT extends DefaultEnvironmentConstants("7076")
  case BASE_PATH extends DefaultEnvironmentConstants(System.getProperty("user.home") + "/tmp")
  case CLEAR_TMP_DIR extends DefaultEnvironmentConstants("true")
  case GIT_HTTP_SERVLET_PATH extends DefaultEnvironmentConstants("/git/*")
}
