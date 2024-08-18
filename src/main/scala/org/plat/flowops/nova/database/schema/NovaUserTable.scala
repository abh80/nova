package org.plat.flowops.nova.database.schema

import org.plat.flowops.nova.database.MyPostgresProfile.api.*

case class NovaUser(username: String, user_id: Long = 0L)

class NovaUserTable(tag: Tag) extends Table[NovaUser](tag, Some("nova"), "nova_user"):
  override def * = (username, user_id).mapTo[NovaUser]

  def user_id = column[Long]("id", O.PrimaryKey)

  def usernameUnique = index("idx_username", username, unique = true)

  def username = column[String]("username")
