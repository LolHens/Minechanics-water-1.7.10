package org.lolhens.minechanics.water


import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraft.world.World

import scala.collection.mutable

/**
 * Created by LolHens on 11.04.2015.
 */
object LiquidHelper {

  def findSource(world: World, x: Int, y: Int, z: Int, filter: (World, Int, Int, Int) => Boolean = (_, _, _, _) => true, ignoreHeight: Boolean = false): Option[(World, Int, Int, Int)] = {
    findSource(world, x, y, z, world.getBlock(x, y, z).getMaterial, filter, ignoreHeight, 512, mutable.MutableList())
  }

  private def findSource(world: World, x: Int, y: Int, z: Int, material: Material, filter: (World, Int, Int, Int) => Boolean, ignoreHeight: Boolean, max: Int, used: mutable.MutableList[(World, Int, Int, Int)]): Option[(World, Int, Int, Int)] = {
    case class Liquid(world: World, xOff: Int, yOff: Int, zOff: Int) {
      val pos = (x + xOff, y + yOff, z + zOff)
      val meta = world.getBlockMetadata(pos._1, pos._2, pos._3)

      def source = meta == 0

      def flowing = meta > 0 && meta < 8

      def falling = meta >= 8
    }

    def getLiquid(world: World, xOff: Int, yOff: Int, zOff: Int) = {
      if (world.getBlock(x + xOff, y + yOff, z + zOff).getMaterial == material)
        new Liquid(world, xOff, yOff, zOff)
      else
        null
    }

    if (max <= 0) return None

    val thisLiquid = getLiquid(world, 0, 0, 0)
    if (thisLiquid == null) return None

    if (used.contains((world, x, y, z))) return None

    (world, x, y, z) +=: used

    val liquids = List(
      thisLiquid,
      getLiquid(world, 0, 1, 0),
      getLiquid(world, 1, 0, 0),
      getLiquid(world, -1, 0, 0),
      getLiquid(world, 0, 0, 1),
      getLiquid(world, 0, 0, -1)
    ).filter(_ != null)

    liquids.foreach(liquid => if (liquid.source && filter(liquid.world, liquid.pos._1, liquid.pos._2, liquid.pos._3)) {
      return Some((liquid.world, liquid.pos._1, liquid.pos._2, liquid.pos._3))
    })

    liquids.foreach(liquid => {
      if (liquid match {
        case liquid if (liquid == thisLiquid) => false
        case liquid if (liquid.yOff == 1) => liquid.falling || thisLiquid.falling
        case liquid => !thisLiquid.falling && (ignoreHeight || liquid.falling || liquid.meta < thisLiquid.meta)
      }) return findSource(liquid.world, liquid.pos._1, liquid.pos._2, liquid.pos._3, material, filter, ignoreHeight, max - 1, used)
    })

    None
  }

  def flowTo(world1: World, x1: Int, y1: Int, z1: Int, world2: World, x2: Int, y2: Int, z2: Int): Unit = {
    world1.setBlock(x1, y1, z1, Blocks.flowing_water, 1, 1 & 2)
    world2.setBlock(x2, y2, z2, Blocks.flowing_water, 0, 1 & 2)
  }
}
