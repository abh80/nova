package org.plat.flowops.nova.service

import org.plat.flowops.nova.utils.TBaseRegistry
import org.quartz.Trigger

import scala.collection.mutable

object JobTriggerRegistry extends TBaseRegistry[String, Trigger]:
  private val registry: mutable.Map[String, Trigger] = mutable.Map()

  override def register(key: String, value: Trigger): Unit = synchronized {
    registry.put(key, value)
  }

  override def get(key: String): Option[Trigger] = synchronized {
    registry.get(key)
  }

  override def remove(key: String): Option[Trigger] = synchronized {
    registry.remove(key)
  }

  override def getAllKeys: Iterable[String] = synchronized {
    registry.keys.toList
  }
