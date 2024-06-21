package org.plat.flowops.nova.database

import javax.sql.DataSource

trait TDatabaseManager:
  /** Connects to the database using the provided DataSource.
    *
    * @param dataSource
    *   the DataSource to connect to
    */
  def connect(dataSource: DataSource): Unit

  /** Returns the type of the database.
    *
    * @return
    *   the database type as a String
    */
  def getType: String

  /** Closes the current database connection if it exists.
    */
  def closeConnection(): Unit

  /** Checks if database is connected
    *
    * @return
    *   true if database is connected else returns false
    */
  def isConnected: Boolean
