package org.plat.flowops.nova.database.schema

import org.plat.flowops.nova.database.MyPostgresProfile.api.*

case class NovaUser(base_path: String, username: String, user_id: Long = 0L)

class NovaUserTable(tag: Tag) extends Table[NovaUser](tag, Some("nova"), "nova_user"):
  override def * = (base_path, username, user_id).mapTo[NovaUser]

  def user_id = column[Long]("id", O.PrimaryKey)

  def username = column[String]("username")

  def base_path = column[String]("base_path")

  def usernameUnique = index("idx_username", username, unique = true)
  def basePathUnique = index("idx_base_path", base_path, unique = true)
