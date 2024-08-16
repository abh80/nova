package org.plat.flowops.nova.database.schema

import org.plat.flowops.nova.database.MyPostgresProfile.api.*

import java.sql.Timestamp

case class NovaRepository(
    owner_id: Long,
    repository_name: String,
    repository_id: Option[Long],
    created_at: Timestamp,
    updated_at: Timestamp,
    isPrivate: Boolean,
    default_branch: String,
    description: String
)

class NovaRepositoryTable(tag: Tag) extends Table[NovaRepository](tag, Some("nova"), "repository"):
  override def * = (
    owner_id,
    repository_name,
    repository_id.?,
    created_at,
    updated_at,
    isPrivate,
    default_branch,
    description
  ) <> ((NovaRepository.apply _).tupled, NovaRepository.unapply)

  def repository_id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def created_at = column[Timestamp]("created_at")

  def updated_at = column[Timestamp]("updated_at")

  def isPrivate = column[Boolean]("isPrivate")

  def default_branch = column[String]("default_branch")

  def description = column[String]("description")

  def owner_id = column[Long]("owner_id")

  def repository_name = column[String]("repository_name")

  def uniqueUsernameRepo = index("idx_username_repository_unique", (owner_id, repository_name), unique = true)
