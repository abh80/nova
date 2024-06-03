package org.plat.flowops.nova.utils

import scala.collection.mutable

trait TBaseRegistry[ID, T] {
  def register(key: ID, value: T): Unit
  def get(key: ID) : Option[T]
  def remove(key: ID): Option[T]
  def getAllKeys: Iterable[String]
}
