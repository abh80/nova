package org.plat.flowops.nova.database

import javax.sql.DataSource

trait TDatabaseManager:
  def connect(dataSource: DataSource): Unit
  def getType: String
  def closeConnection(): Unit
