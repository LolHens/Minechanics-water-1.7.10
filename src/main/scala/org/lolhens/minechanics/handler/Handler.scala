package org.lolhens.minechanics.handler

import net.minecraft.block.Block
import net.minecraft.world.World
import org.lolhens.minechanics.water.LiquidHelper

/**
 * Created by LolHens on 11.04.2015.
 */
object Handler {
  def onWaterFlowdown(world: World, x: Int, y: Int, z: Int, meta: Int, block: Block) = {
    if (world.getBlock(x, y + 1, z).getMaterial == block.getMaterial) {
      val source = LiquidHelper.findSource(world, x, y + 1, z).getOrElse(null)
      if (source != null) {
        val (sWorld: World, sX: Int, sY: Int, sZ: Int) = source
        LiquidHelper.flowTo(sWorld, sX, sY, sZ, world, x, y, z)
      }
    }
  }
}
