package org.plat.flowops.nova.database.schema

import org.plat.flowops.nova.database.MyPostgresProfile.api.*
import slick.lifted.ProvenShape

case class NovaRepositoryAccessPolicy(
    access_policy_id: Long,
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

  def access_policy_id = column[Long]("access_policy_id", O.PrimaryKey)
  def repository_id    = column[Long]("repository_id")
  def user_id          = column[Long]("user_id")
  def access_level     = column[String]("access_level")
