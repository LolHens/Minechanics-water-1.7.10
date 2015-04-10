package org.lolhens.minechanics.core.obfuscate

import scala.collection.mutable

/**
 * Created by LolHens on 10.04.2015.
 */
object ObfMapper {
  private val obfMap = mutable.Map[String, String]()

  addMapping("func_149674_a", "updateTick")

  def apply(obfName: String): String = obfMap.get(obfName) match {
    case Some(name) => obfName
    case None => obfName
  }

  def addMapping(mapping: (String, String)) = obfMap += mapping
}
