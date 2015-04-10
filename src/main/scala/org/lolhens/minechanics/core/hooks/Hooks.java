package org.lolhens.minechanics.core.hooks;

import net.minecraft.world.World;

/**
 * Created by LolHens on 11.04.2015.
 */
public class Hooks {
    public static void onWaterFlowdown(World world, int x, int y, int z, int newmeta) {
        System.out.println(x + " " + y + " " + z);
    }
}
