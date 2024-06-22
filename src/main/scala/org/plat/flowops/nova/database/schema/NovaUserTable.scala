package org.plat.flowops.nova.database.schema

import org.plat.flowops.nova.database.MyPostgresProfile.api.*

case class NovaUser(base_path: String, user_id: Long = 0L)

class NovaUserTable(tag: Tag) extends Table[NovaUser](tag, Some("nova"), "nova_user"):
  override def * = (base_path, user_id).mapTo[NovaUser]

  def user_id = column[Long]("id", O.PrimaryKey)

  def base_path = column[String]("base_path")
