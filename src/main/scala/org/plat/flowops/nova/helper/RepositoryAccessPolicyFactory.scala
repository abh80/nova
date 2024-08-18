package org.plat.flowops.nova.helper

import org.plat.flowops.nova.constants.RepositoryAccessLevelType
import org.plat.flowops.nova.database.schema.NovaRepositoryAccessPolicy

trait RepositoryAccessPolicyFactory {}

object RepositoryAccessPolicyFactory:
  def generatePolicy(
      user_id: Long,
      repository_id: Long,
      repositoryAccessLevelType: RepositoryAccessLevelType
  ): NovaRepositoryAccessPolicy =
    val policy_id = java.util.UUID.randomUUID()
    NovaRepositoryAccessPolicy(
      f"FLRAT-$policy_id",
      repository_id,
      user_id,
      access_level = repositoryAccessLevelType.toString
    )
