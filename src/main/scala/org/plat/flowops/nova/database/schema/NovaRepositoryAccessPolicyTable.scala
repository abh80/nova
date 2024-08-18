package org.plat.flowops.nova.database.schema

import org.plat.flowops.nova.database.MyPostgresProfile.api.*
import slick.lifted.ProvenShape

case class NovaRepositoryAccessPolicy(
    access_policy_id: String,
    repository_id: Long,
    user_id: Long,
    access_level: String
)

class NovaRepositoryAccessPolicyTable(tag: Tag)
    extends Table[NovaRepositoryAccessPolicy](tag, Some("nova"), "repository_access_policy"):
  override def * = (
    access_policy_id,
    repository_id,
    user_id,
    access_level
  ) <> ((NovaRepositoryAccessPolicy.apply _).tupled, NovaRepositoryAccessPolicy.unapply)

  def access_policy_id = column[String]("access_policy_id", O.PrimaryKey)
  def repository_id    = column[Long]("repository_id")
  def user_id          = column[Long]("user_id")
  def access_level     = column[String]("access_level")

  def userRepositoryIndex = index("idx_user_id_repository_id_unique", (user_id, repository_id), unique = true)
