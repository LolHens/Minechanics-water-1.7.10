package org.lolhens.minechanics.core.hooks;

import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * Created by LolHens on 11.04.2015.
 */
public class Hooks {
    public static void onWaterFlowdown(World world, int x, int y, int z, int meta) {
        Handler.onWaterFlowdown(world, x, y, z, meta);
    }

    /*public boolean removeSource(World world, int x, int y, int z, boolean ignoreHeight, boolean readOnly, int maxBlocks) {
        return MainConfig.liquid_mechanicEnabled
                && removeSource(world, x, y, z, 0, 0, ignoreHeight, readOnly, maxBlocks, new LinkedList<int[]>()) == 2;
    }

    private int removeSource(World world, int x, int y, int z, int dir, int ret, boolean ignoreHeight, boolean readOnly,
                             int maxBlocks, List<int[]> useless) {
        if (maxBlocks <= 0) return 0;
        if (isLiquid(world, x, y, z, 0)) {
            for (int[] uselessPos : useless)
                if (uselessPos[0] == x && uselessPos[2] == z && uselessPos[1] == y) return 0;
            useless.add(new int[] { x, y, z });

            int[] metalist = { -1, -1, -1, -1, -1, -1 };

            metalist[0] = world.getBlockMetadata(x, y, z);
            if (isLiquid(world, x, y + 1, z, 0)) metalist[1] = world.getBlockMetadata(x, y + 1, z);
            if (isLiquid(world, x + 1, y, z, 0)) metalist[2] = world.getBlockMetadata(x + 1, y, z);
            if (isLiquid(world, x - 1, y, z, 0)) metalist[3] = world.getBlockMetadata(x - 1, y, z);
            if (isLiquid(world, x, y, z + 1, 0)) metalist[4] = world.getBlockMetadata(x, y, z + 1);
            if (isLiquid(world, x, y, z - 1, 0)) metalist[5] = world.getBlockMetadata(x, y, z - 1);

            ret = foundSource(world, x, y, z, metalist[0], readOnly);
            if (ret > 0) return ret;
            ret = foundSource(world, x, y + 1, z, metalist[1], readOnly);
            if (ret > 0) return ret;
            ret = foundSource(world, x + 1, y, z, metalist[2], readOnly);
            if (ret > 0) return ret;
            ret = foundSource(world, x - 1, y, z, metalist[3], readOnly);
            if (ret > 0) return ret;
            ret = foundSource(world, x, y, z + 1, metalist[4], readOnly);
            if (ret > 0) return ret;
            ret = foundSource(world, x, y, z - 1, metalist[5], readOnly);
            if (ret > 0) return ret;

            if (metalist[1] >= 8 || metalist[0] >= 8) {
                ret = removeSource(world, x, y + 1, z, 0, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                if (ret > 0) return ret;
            }
            if (dir != 2 && metalist[0] < 8 && (metalist[2] >= 8 || metalist[2] < metalist[0])) {
                ret = removeSource(world, x + 1, y, z, 1, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                if (ret > 0) return ret;
            }
            if (dir != 1 && metalist[0] < 8 && (metalist[3] >= 8 || metalist[3] < metalist[0])) {
                ret = removeSource(world, x - 1, y, z, 2, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                if (ret > 0) return ret;
            }
            if (dir != 4 && metalist[0] < 8 && (metalist[4] >= 8 || metalist[4] < metalist[0])) {
                ret = removeSource(world, x, y, z + 1, 3, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                if (ret > 0) return ret;
            }
            if (dir != 3 && metalist[0] < 8 && (metalist[5] >= 8 || metalist[5] < metalist[0])) {
                ret = removeSource(world, x, y, z - 1, 4, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                if (ret > 0) return ret;
            }

            if (ignoreHeight) {
                if (dir != 2 && metalist[0] < 8 && metalist[2] < 8 && ignoreHeight) {
                    ret = removeSource(world, x + 1, y, z, 1, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                    if (ret > 0) return ret;
                }
                if (dir != 1 && metalist[0] < 8 && metalist[3] < 8 && ignoreHeight) {
                    ret = removeSource(world, x - 1, y, z, 2, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                    if (ret > 0) return ret;
                }
                if (dir != 4 && metalist[0] < 8 && metalist[4] < 8 && ignoreHeight) {
                    ret = removeSource(world, x, y, z + 1, 3, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                    if (ret > 0) return ret;
                }
                if (dir != 3 && metalist[0] < 8 && metalist[5] < 8 && ignoreHeight) {
                    ret = removeSource(world, x, y, z - 1, 4, 0, ignoreHeight, readOnly, maxBlocks - 1, useless);
                    if (ret > 0) return ret;
                }
            }
        }
        return 0;
    }

    private int foundSource(World world, int x, int y, int z, int meta, boolean readOnly) {
        if (meta == -1 || meta > 0) return 0;
        Block myBlock = world.getBlock(x, y - 1, z);
        if (MainConfig.liquid_vanillaMechanicSpawner != null && myBlock == MainConfig.liquid_vanillaMechanicSpawner) return 1;
        if ((MainConfig.liquid_infiniteLiquidSource == null || myBlock != MainConfig.liquid_infiniteLiquidSource) && !readOnly) {
            setLiquid(world, x, y, z, 0, 1, Notify.NEIGHBORS);
        }
        return 2;
    }*/
}
