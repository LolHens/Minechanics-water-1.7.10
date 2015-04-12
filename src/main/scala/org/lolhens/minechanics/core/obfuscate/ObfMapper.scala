package org.lolhens.minechanics.core.obfuscate

import net.minecraft.block.Block
import net.minecraft.launchwrapper.Launch
import net.minecraft.world.storage.WorldInfo
import org.lolhens.minechanics.LogHelper

import scala.collection.mutable

/**
 * Created by LolHens on 10.04.2015.
 */
object ObfMapper {
  private val obfMap = mutable.Map[String, String]()

  addMapping("func_149674_a", "updateTick")
  addMapping("field_149764_J", "blockMaterial")
  addMapping("func_72805_g", "getBlockMetadata")
  addMapping("func_149688_o", "getMaterial")
  addMapping("func_147439_a", "getBlock")

  val deobfuscated = Launch.blackboard.get("fml.deobfuscatedEnvironment").asInstanceOf[java.lang.Boolean].booleanValue()

  def apply(obfName: String): String = obfMap.get(obfName) match {
    case Some(name) => name
    case None => obfName
  }

  def obf(name: String): String = obfMap.find(set => (set._2 == name)) match {
    case Some(set) if (!deobfuscated) => set._1
    case _ => name
  }

  def addMapping(mapping: (String, String)) = obfMap += mapping
}
