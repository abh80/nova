package org.plat.flowops.nova.helper

import com.typesafe.scalalogging.LazyLogging
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.lib.Repository
import org.plat.flowops.nova.service.SystemService

import java.io.File

trait RepositoryFactory extends SystemService with LazyLogging:
  def getRepository(ownerId: Long, repositoryId: Long): Repository =
    val dir = new File(s"${DIRECTORY.REPO_DIR}/$ownerId/$repositoryId.git")

    logger.debug(s"Getting repository: ${dir.getAbsolutePath}")

    if !dir.exists() then
      logger.debug(s"Repository directory does not exist, creating one...")
      if !dir.mkdirs() then
        logger.error(s"Failed to create repository directory: ${dir.getAbsolutePath}")
        throw new RuntimeException(s"Failed to create repository directory: ${dir.getAbsolutePath}")
      else return initGitRepository(dir)

    new FileRepository(dir)

  private def initGitRepository(dir: File): Repository =
    logger.debug(s"Initializing an empty git repository: ${dir.getAbsolutePath}")

    Git
      .init()
      .setBare(true)
      .setDirectory(dir)
      .call()
      .getRepository
