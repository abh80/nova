package org.plat.flowops.nova.registry

import org.plat.flowops.nova.utils.TBaseRegistry

import java.util.concurrent.locks.{ Lock, ReentrantLock }
import scala.collection.mutable

object LockRegistry extends TBaseRegistry[String, Lock]:

  private val registry: mutable.Map[String, Lock] = mutable.Map()

  override def remove(key: String): Option[Lock] = synchronized {
    registry.remove(key)
  }

  override def getAllKeys: Iterable[String] = synchronized {
    registry.keys.toList
  }

  def lock[T](key: String)(f: => T): T =
    var lock = get(key)
    if lock.isEmpty then
      lock = Some(new ReentrantLock())
      register(key, lock.get)
    try
      lock.get.lock()
      f
    finally lock.get.unlock()

  override def register(key: String, value: Lock): Unit = synchronized {
    registry.put(key, value)
  }

  override def get(key: String): Option[Lock] = synchronized {
    registry.get(key)
  }
