package org.plat.flowops.nova.database

trait TDatabaseManager {
  def connect(connectionURL: String, username: String, password: String) : Unit
  def getType : String
  def closeConnection(): Unit
}
