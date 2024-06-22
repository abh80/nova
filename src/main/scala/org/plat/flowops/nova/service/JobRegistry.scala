package org.plat.flowops.nova.service

import org.plat.flowops.nova.utils.TBaseRegistry
import org.quartz.JobDetail

import scala.collection.mutable

object JobRegistry extends TBaseRegistry[String, JobDetail]:
  private val registry: mutable.Map[String, JobDetail] = mutable.Map()

  override def register(key: String, value: JobDetail): Unit = synchronized {
    registry.put(key, value)
  }

  override def get(key: String): Option[JobDetail] = synchronized {
    registry.get(key)
  }

  override def remove(key: String): Option[JobDetail] = synchronized {
    registry.remove(key)
  }

  override def getAllKeys: Iterable[String] = synchronized {
    registry.keys.toList
  }
