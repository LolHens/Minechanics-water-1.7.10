package org.lolhens.minechanics.tmp;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDynamicLiquid2 extends BlockLiquid2 {
    int numSourceBlocks;
    boolean[] flowSideways = new boolean[4];
    int[] sidewaysFlowPreference = new int[4];
    private static final String __OBFID = "CL_00000234";

    protected BlockDynamicLiquid2(Material mat) {
        super(mat);
    }

    private void setLiquidStatic(World world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, getBlockById(getIdFromBlock(this) + 1), l, 2);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World worldIn, int x, int y, int z, Random random) {
        int fluidMeta = this.getFluidMeta(worldIn, x, y, z);
        byte metaStepSize = 1;

        if (this.blockMaterial == Material.lava && !worldIn.provider.isHellWorld) {
            metaStepSize = 2;
        }

        boolean flag = true;
        int i1 = this.tickRate(worldIn);
        int nextMeta;

        if (fluidMeta > 0) {
            byte b1 = -100;
            this.numSourceBlocks = 0;
            int lvl = this.getHighestFluidMeta(worldIn, x - 1, y, z, b1);
            lvl = this.getHighestFluidMeta(worldIn, x + 1, y, z, lvl);
            lvl = this.getHighestFluidMeta(worldIn, x, y, z - 1, lvl);
            lvl = this.getHighestFluidMeta(worldIn, x, y, z + 1, lvl);
            nextMeta = lvl + metaStepSize;

            if (nextMeta >= 8 || lvl < 0) {
                nextMeta = -1;
            }

            if (this.getFluidMeta(worldIn, x, y + 1, z) >= 0) {
                int k1 = this.getFluidMeta(worldIn, x, y + 1, z);

                if (k1 >= 8) {
                    nextMeta = k1;
                } else {
                    nextMeta = k1 + 8;
                }
            }

            if (this.numSourceBlocks >= 2 && this.blockMaterial == Material.water) {
                if (worldIn.getBlock(x, y - 1, z).getMaterial().isSolid()) {
                    nextMeta = 0;
                } else if (worldIn.getBlock(x, y - 1, z).getMaterial() == this.blockMaterial && worldIn.getBlockMetadata(x, y - 1, z) == 0) {
                    nextMeta = 0;
                }
            }

            if (this.blockMaterial == Material.lava && fluidMeta < 8 && nextMeta < 8 && nextMeta > fluidMeta && random.nextInt(4) != 0) {
                i1 *= 4;
            }

            if (nextMeta == fluidMeta) {
                if (flag) {
                    this.setLiquidStatic(worldIn, x, y, z);
                }
            } else {
                fluidMeta = nextMeta;

                if (nextMeta < 0) {
                    worldIn.setBlockToAir(x, y, z);
                } else {
                    worldIn.setBlockMetadataWithNotify(x, y, z, nextMeta, 2);
                    worldIn.scheduleBlockUpdate(x, y, z, this, i1);
                    worldIn.notifyBlocksOfNeighborChange(x, y, z, this);
                }
            }
        } else {
            this.setLiquidStatic(worldIn, x, y, z);
        }

        if (this.canWashAway(worldIn, x, y - 1, z)) {
            if (this.blockMaterial == Material.lava && worldIn.getBlock(x, y - 1, z).getMaterial() == Material.water) {
                worldIn.setBlock(x, y - 1, z, Blocks.stone);
                this.evaporateEffect(worldIn, x, y - 1, z);
                return;
            }

            if (fluidMeta >= 8) {
                this.washAway(worldIn, x, y - 1, z, fluidMeta);
            } else {
                this.washAway(worldIn, x, y - 1, z, fluidMeta + 8);
            }
        } else if (fluidMeta >= 0 && (fluidMeta == 0 || this.isWaterproof(worldIn, x, y - 1, z))) {
            boolean[] flowSideways = this.shouldFlowSideways(worldIn, x, y, z);
            nextMeta = fluidMeta + metaStepSize;

            if (fluidMeta >= 8) {
                nextMeta = 1;
            }

            if (nextMeta >= 8) {
                return;
            }

            if (flowSideways[0]) {
                this.washAway(worldIn, x - 1, y, z, nextMeta);
            }

            if (flowSideways[1]) {
                this.washAway(worldIn, x + 1, y, z, nextMeta);
            }

            if (flowSideways[2]) {
                this.washAway(worldIn, x, y, z - 1, nextMeta);
            }

            if (flowSideways[3]) {
                this.washAway(worldIn, x, y, z + 1, nextMeta);
            }
        }
    }

    private void washAway(World world, int x, int y, int z, int meta) {
        if (blockMaterial == world.getBlock(x, y, z).getMaterial()) return;
        if (this.canWashAway(world, x, y, z)) {
            Block block = world.getBlock(x, y, z);

            if (this.blockMaterial == Material.lava) {
                this.evaporateEffect(world, x, y, z);
            } else {
                block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            }

            world.setBlock(x, y, z, this, meta, 3);
        }
    }

    private int sidewaysFlowPreference(World world, int x, int y, int z, int meta, int side) {
        int j1 = 1000;

        for (int side2 = 0; side2 < 4; ++side2) {
            if ((side2 != 0 || side != 1) && (side2 != 1 || side != 0) && (side2 != 2 || side != 3) && (side2 != 3 || side != 2)) {
                int tmpX = x;
                int tmpZ = z;

                if (side2 == 0) {
                    tmpX = x - 1;
                }

                if (side2 == 1) {
                    ++tmpX;
                }

                if (side2 == 2) {
                    tmpZ = z - 1;
                }

                if (side2 == 3) {
                    ++tmpZ;
                }

                if (!this.isWaterproof(world, tmpX, y, tmpZ) && (world.getBlock(tmpX, y, tmpZ).getMaterial() != this.blockMaterial || world.getBlockMetadata(tmpX, y, tmpZ) != 0)) {
                    if (!this.isWaterproof(world, tmpX, y - 1, tmpZ)) {
                        return meta;
                    }

                    if (meta < 4) {
                        int j2 = this.sidewaysFlowPreference(world, tmpX, y, tmpZ, meta + 1, side2);

                        if (j2 < j1) {
                            j1 = j2;
                        }
                    }
                }
            }
        }

        return j1;
    }

    private boolean[] shouldFlowSideways(World world, int x, int y, int z) {
        int side;
        int tmpX, tmpZ;

        for (side = 0; side < 4; ++side) {
            this.sidewaysFlowPreference[side] = 1000;
            tmpX = x;
            tmpZ = z;

            if (side == 0) {
                tmpX = x - 1;
            }

            if (side == 1) {
                ++tmpX;
            }

            if (side == 2) {
                tmpZ = z - 1;
            }

            if (side == 3) {
                ++tmpZ;
            }

            if (!this.isWaterproof(world, tmpX, y, tmpZ) && (world.getBlock(tmpX, y, tmpZ).getMaterial() != this.blockMaterial || world.getBlockMetadata(tmpX, y, tmpZ) != 0)) {
                if (this.isWaterproof(world, tmpX, y - 1, tmpZ)) {
                    this.sidewaysFlowPreference[side] = this.sidewaysFlowPreference(world, tmpX, y, tmpZ, 1, side);
                } else {
                    this.sidewaysFlowPreference[side] = 0;
                }
            }
        }

        side = this.sidewaysFlowPreference[0];

        for (tmpX = 1; tmpX < 4; ++tmpX) {
            if (this.sidewaysFlowPreference[tmpX] < side) {
                side = this.sidewaysFlowPreference[tmpX];
            }
        }

        for (tmpX = 0; tmpX < 4; ++tmpX) {
            this.flowSideways[tmpX] = this.sidewaysFlowPreference[tmpX] == side;
        }

        return this.flowSideways;
    }

    private boolean isWaterproof(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block.getMaterial() == this.blockMaterial && world.getBlockMetadata(x, y, z) == 0) return true;
        return block != Blocks.wooden_door && block != Blocks.iron_door && block != Blocks.standing_sign && block != Blocks.ladder && block != Blocks.reeds ? (block.getMaterial() == Material.portal ? true : block.getMaterial().blocksMovement()) : true;
    }

    protected int getHighestFluidMeta(World world, int x, int y, int z, int meta) {
        int meta2 = this.getFluidMeta(world, x, y, z);

        if (meta2 < 0) {
            return meta;
        } else {
            if (meta2 == 0) {
                ++this.numSourceBlocks;
            }

            if (meta2 >= 8) {
                meta2 = 0;
            }

            return meta >= 0 && meta2 >= meta ? meta : meta2;
        }
    }

    private boolean canWashAway(World world, int x, int y, int z) {
        Material material = world.getBlock(x, y, z).getMaterial();
        if (material == this.blockMaterial && world.getBlockMetadata(x, y, z) > 0 && world.getBlockMetadata(x, y, z) < 8)
            return true;
        return material == this.blockMaterial ? false : (material == Material.lava ? false : !this.isWaterproof(world, x, y, z));
    }

    public void onBlockAdded(World worldIn, int x, int y, int z) {
        super.onBlockAdded(worldIn, x, y, z);

        if (worldIn.getBlock(x, y, z) == this) {
            worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn));
        }
    }

    public boolean requiresUpdates() {
        return true;
    }
}