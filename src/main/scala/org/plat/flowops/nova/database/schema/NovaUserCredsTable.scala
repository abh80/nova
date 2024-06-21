package org.plat.flowops.nova.database.schema

import org.plat.flowops.nova.database.MyPostgresProfile.MyAPI.strListTypeMapper
import org.plat.flowops.nova.database.MyPostgresProfile.api.*

import java.sql.Timestamp

case class NovaUserCreds(
    user_id: Long,
    key: String,
    created_at: Timestamp,
    expires_at: Timestamp,
    scope: List[String],
    key_id: Long
)

class NovaUserCredsTable(tag: Tag) extends Table[NovaUserCreds](tag, Some("nova"), "user_credentials"):
  override def * = (
    user_id,
    key,
    created_at,
    expires_at,
    scope,
    key_id
  ) <> ((NovaUserCreds.apply _).tupled, NovaUserCreds.unapply)

  def user_id = column[Long]("user_id")

  def key = column[String]("key")

  def key_id = column[Long]("id", O.PrimaryKey)

  def expires_at = column[Timestamp]("expires_at")

  def created_at = column[Timestamp]("created_at")

  def scope = column[List[String]]("scope")
