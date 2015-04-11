package org.lolhens.minechanics.core.hooks;

import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * Created by LolHens on 11.04.2015.
 */
public class Hooks {
    public static void onWaterFlowdown(Block block, World world, int x, int y, int z, int meta) {
        Handler.onWaterFlowdown(world, x, y, z, meta, block);
    }
}
