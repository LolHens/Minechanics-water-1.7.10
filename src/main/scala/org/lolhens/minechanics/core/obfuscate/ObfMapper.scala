package org.lolhens.minechanics.core.obfuscate

import scala.collection.mutable

/**
 * Created by LolHens on 10.04.2015.
 */
object ObfMapper {
  private val obfMap = mutable.Map[String, String]()

  addMapping("updateTick", "func_149674_a")

  def apply(name: String): String = obfMap.get(name) match {
    case Some(obfName) => obfName
    case None => name
  }

  def addMapping(mapping: (String, String)) = obfMap += mapping
}
