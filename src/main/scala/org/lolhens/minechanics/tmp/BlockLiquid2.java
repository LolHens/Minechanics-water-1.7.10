package org.lolhens.minechanics.tmp;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockLiquid2 extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] icon;
    private static final String __OBFID = "CL_00000265";

    protected BlockLiquid2(Material p_i45413_1_) {
        super(p_i45413_1_);
        float f = 0.0F;
        float f1 = 0.0F;
        this.setBlockBounds(0.0F + f1, 0.0F + f, 0.0F + f1, 1.0F + f1, 1.0F + f, 1.0F + f1);
        this.setTickRandomly(true);
    }

    public static BlockLiquid2 to(BlockLiquid b) {
        return null;
    }

    public boolean isPassable(IBlockAccess worldIn, int x, int y, int z) {
        return this.blockMaterial != Material.lava;
    }

    /**
     * Returns the percentage of the liquid block that is air, based on the given flow decay of the liquid
     */
    public static float getLiquidHeightPercent(int p_149801_0_) {
        if (p_149801_0_ >= 8) {
            p_149801_0_ = 0;
        }

        return (float) (p_149801_0_ + 1) / 9.0F;
    }

    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return 16777215;
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z) {
        if (this.blockMaterial != Material.water) {
            return 16777215;
        } else {
            int l = 0;
            int i1 = 0;
            int j1 = 0;

            for (int k1 = -1; k1 <= 1; ++k1) {
                for (int l1 = -1; l1 <= 1; ++l1) {
                    int i2 = worldIn.getBiomeGenForCoords(x + l1, z + k1).getWaterColorMultiplier();
                    l += (i2 & 16711680) >> 16;
                    i1 += (i2 & 65280) >> 8;
                    j1 += i2 & 255;
                }
            }

            return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
        }
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return side != 0 && side != 1 ? this.icon[1] : this.icon[0];
    }

    protected int getFluidMeta(World world, int x, int y, int z) {
        return world.getBlock(x, y, z).getMaterial() == this.blockMaterial ? world.getBlockMetadata(x, y, z) : -1;
    }

    /**
     * Returns the flow decay but converts values indicating falling liquid (values >=8) to their effective source block
     * value of zero
     */
    protected int getEffectiveFlowDecay(IBlockAccess blockAccess, int x, int y, int z) {
        if (blockAccess.getBlock(x, y, z).getMaterial() != this.blockMaterial) {
            return -1;
        } else {
            int l = blockAccess.getBlockMetadata(x, y, z);

            if (l >= 8) {
                l = 0;
            }

            return l;
        }
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * Returns whether the raytracing must ignore this block. Args : metadata, stopOnLiquid
     */
    public boolean canStopRayTrace(int meta, boolean includeLiquid) {
        return includeLiquid && meta == 0;
    }

    public boolean isBlockSolid(IBlockAccess worldIn, int x, int y, int z, int side) {
        Material material = worldIn.getBlock(x, y, z).getMaterial();
        return material == this.blockMaterial ? false : (side == 1 ? true : (material == Material.ice ? false : super.isBlockSolid(worldIn, x, y, z, side)));
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side) {
        Material material = worldIn.getBlock(x, y, z).getMaterial();
        return material == this.blockMaterial ? false : (side == 1 ? true : super.shouldSideBeRendered(worldIn, x, y, z, side));
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return null;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return 4;
    }

    public Item getItemDropped(int meta, Random random, int fortune) {
        return null;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random) {
        return 0;
    }

    /**
     * Returns a vector indicating the direction and intensity of liquid flow
     */
    private Vec3 getFlowVector(IBlockAccess blockAccess, int x, int y, int z) {
        Vec3 vec3 = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
        int l = this.getEffectiveFlowDecay(blockAccess, x, y, z);

        for (int i1 = 0; i1 < 4; ++i1) {
            int j1 = x;
            int k1 = z;

            if (i1 == 0) {
                j1 = x - 1;
            }

            if (i1 == 1) {
                k1 = z - 1;
            }

            if (i1 == 2) {
                ++j1;
            }

            if (i1 == 3) {
                ++k1;
            }

            int l1 = this.getEffectiveFlowDecay(blockAccess, j1, y, k1);
            int i2;

            if (l1 < 0) {
                if (!blockAccess.getBlock(j1, y, k1).getMaterial().blocksMovement()) {
                    l1 = this.getEffectiveFlowDecay(blockAccess, j1, y - 1, k1);

                    if (l1 >= 0) {
                        i2 = l1 - (l - 8);
                        vec3 = vec3.addVector((double) ((j1 - x) * i2), (double) ((y - y) * i2), (double) ((k1 - z) * i2));
                    }
                }
            } else if (l1 >= 0) {
                i2 = l1 - l;
                vec3 = vec3.addVector((double) ((j1 - x) * i2), (double) ((y - y) * i2), (double) ((k1 - z) * i2));
            }
        }

        if (blockAccess.getBlockMetadata(x, y, z) >= 8) {
            boolean flag = false;

            if (flag || this.isBlockSolid(blockAccess, x, y, z - 1, 2)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(blockAccess, x, y, z + 1, 3)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(blockAccess, x - 1, y, z, 4)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(blockAccess, x + 1, y, z, 5)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(blockAccess, x, y + 1, z - 1, 2)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(blockAccess, x, y + 1, z + 1, 3)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(blockAccess, x - 1, y + 1, z, 4)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(blockAccess, x + 1, y + 1, z, 5)) {
                flag = true;
            }

            if (flag) {
                vec3 = vec3.normalize().addVector(0.0D, -6.0D, 0.0D);
            }
        }

        vec3 = vec3.normalize();
        return vec3;
    }

    public void modifyEntityVelocity(World worldIn, int x, int y, int z, Entity entityIn, Vec3 velocity) {
        Vec3 vec31 = this.getFlowVector(worldIn, x, y, z);
        velocity.xCoord += vec31.xCoord;
        velocity.yCoord += vec31.yCoord;
        velocity.zCoord += vec31.zCoord;
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn) {
        return this.blockMaterial == Material.water ? 5 : (this.blockMaterial == Material.lava ? (worldIn.provider.hasNoSky ? 10 : 30) : 0);
    }

    /**
     * How bright to render this block based on the light its receiving. Args: iBlockAccess, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess blockAccess, int x, int y, int z) {
        int l = blockAccess.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int i1 = blockAccess.getLightBrightnessForSkyBlocks(x, y + 1, z, 0);
        int j1 = l & 255;
        int k1 = i1 & 255;
        int l1 = l >> 16 & 255;
        int i2 = i1 >> 16 & 255;
        return (j1 > k1 ? j1 : k1) | (l1 > i2 ? l1 : i2) << 16;
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return this.blockMaterial == Material.water ? 1 : 0;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        int l;

        if (this.blockMaterial == Material.water) {
            if (random.nextInt(10) == 0) {
                l = world.getBlockMetadata(x, y, z);

                if (l <= 0 || l >= 8) {
                    world.spawnParticle("suspended", (double) ((float) x + random.nextFloat()), (double) ((float) y + random.nextFloat()), (double) ((float) z + random.nextFloat()), 0.0D, 0.0D, 0.0D);
                }
            }

            for (l = 0; l < 0; ++l) {
                int i1 = random.nextInt(4);
                int j1 = x;
                int k1 = z;

                if (i1 == 0) {
                    j1 = x - 1;
                }

                if (i1 == 1) {
                    ++j1;
                }

                if (i1 == 2) {
                    k1 = z - 1;
                }

                if (i1 == 3) {
                    ++k1;
                }

                if (world.getBlock(j1, y, k1).getMaterial() == Material.air && (world.getBlock(j1, y - 1, k1).getMaterial().blocksMovement() || world.getBlock(j1, y - 1, k1).getMaterial().isLiquid())) {
                    float f = 0.0625F;
                    double d0 = (double) ((float) x + random.nextFloat());
                    double d1 = (double) ((float) y + random.nextFloat());
                    double d2 = (double) ((float) z + random.nextFloat());

                    if (i1 == 0) {
                        d0 = (double) ((float) x - f);
                    }

                    if (i1 == 1) {
                        d0 = (double) ((float) (x + 1) + f);
                    }

                    if (i1 == 2) {
                        d2 = (double) ((float) z - f);
                    }

                    if (i1 == 3) {
                        d2 = (double) ((float) (z + 1) + f);
                    }

                    double d3 = 0.0D;
                    double d4 = 0.0D;

                    if (i1 == 0) {
                        d3 = (double) (-f);
                    }

                    if (i1 == 1) {
                        d3 = (double) f;
                    }

                    if (i1 == 2) {
                        d4 = (double) (-f);
                    }

                    if (i1 == 3) {
                        d4 = (double) f;
                    }

                    world.spawnParticle("splash", d0, d1, d2, d3, 0.0D, d4);
                }
            }
        }

        if (this.blockMaterial == Material.water && random.nextInt(64) == 0) {
            l = world.getBlockMetadata(x, y, z);

            if (l > 0 && l < 8) {
                world.playSound((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), "liquid.water", random.nextFloat() * 0.25F + 0.75F, random.nextFloat() * 1.0F + 0.5F, false);
            }
        }

        double d5;
        double d6;
        double d7;

        if (this.blockMaterial == Material.lava && world.getBlock(x, y + 1, z).getMaterial() == Material.air && !world.getBlock(x, y + 1, z).isOpaqueCube()) {
            if (random.nextInt(100) == 0) {
                d5 = (double) ((float) x + random.nextFloat());
                d6 = (double) y + this.maxY;
                d7 = (double) ((float) z + random.nextFloat());
                world.spawnParticle("lava", d5, d6, d7, 0.0D, 0.0D, 0.0D);
                world.playSound(d5, d6, d7, "liquid.lavapop", 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }

            if (random.nextInt(200) == 0) {
                world.playSound((double) x, (double) y, (double) z, "liquid.lava", 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }

        if (random.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && !world.getBlock(x, y - 2, z).getMaterial().blocksMovement()) {
            d5 = (double) ((float) x + random.nextFloat());
            d6 = (double) y - 1.05D;
            d7 = (double) ((float) z + random.nextFloat());

            if (this.blockMaterial == Material.water) {
                world.spawnParticle("dripWater", d5, d6, d7, 0.0D, 0.0D, 0.0D);
            } else {
                world.spawnParticle("dripLava", d5, d6, d7, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        this.tryReacting(world, x, y, z);
    }

    /**
     * the sin and cos of this number determine the surface gradient of the flowing block.
     */
    @SideOnly(Side.CLIENT)
    public static double getFlowDirection(IBlockAccess blockAccess, int x, int y, int z, Material mat) {
        Vec3 vec3 = null;

        if (mat == Material.water) {
            vec3 = to(Blocks.flowing_water).getFlowVector(blockAccess, x, y, z);
        }

        if (mat == Material.lava) {
            vec3 = to(Blocks.flowing_lava).getFlowVector(blockAccess, x, y, z);
        }

        return vec3.xCoord == 0.0D && vec3.zCoord == 0.0D ? -1000.0D : Math.atan2(vec3.zCoord, vec3.xCoord) - (Math.PI / 2D);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        this.tryReacting(world, x, y, z);
    }

    private void tryReacting(World world, int x, int y, int z) {
        if (world.getBlock(x, y, z) == this) {
            if (this.blockMaterial == Material.lava) {
                boolean flag = false;

                if (flag || world.getBlock(x, y, z - 1).getMaterial() == Material.water) {
                    flag = true;
                }

                if (flag || world.getBlock(x, y, z + 1).getMaterial() == Material.water) {
                    flag = true;
                }

                if (flag || world.getBlock(x - 1, y, z).getMaterial() == Material.water) {
                    flag = true;
                }

                if (flag || world.getBlock(x + 1, y, z).getMaterial() == Material.water) {
                    flag = true;
                }

                if (flag || world.getBlock(x, y + 1, z).getMaterial() == Material.water) {
                    flag = true;
                }

                if (flag) {
                    int l = world.getBlockMetadata(x, y, z);

                    if (l == 0) {
                        world.setBlock(x, y, z, Blocks.obsidian);
                    } else if (l <= 4) {
                        world.setBlock(x, y, z, Blocks.cobblestone);
                    }

                    this.evaporateEffect(world, x, y, z);
                }
            }
        }
    }

    protected void evaporateEffect(World world, int x, int y, int z) {
        world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

        for (int l = 0; l < 8; ++l) {
            world.spawnParticle("largesmoke", (double) x + Math.random(), (double) y + 1.2D, (double) z + Math.random(), 0.0D, 0.0D, 0.0D);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        if (this.blockMaterial == Material.lava) {
            this.icon = new IIcon[]{reg.registerIcon("lava_still"), reg.registerIcon("lava_flow")};
        } else {
            this.icon = new IIcon[]{reg.registerIcon("water_still"), reg.registerIcon("water_flow")};
        }
    }

    @SideOnly(Side.CLIENT)
    public static IIcon getLiquidIcon(String p_149803_0_) {
        return p_149803_0_ == "water_still" ? to(Blocks.flowing_water).icon[0] : (p_149803_0_ == "water_flow" ? to(Blocks.flowing_water).icon[1] : (p_149803_0_ == "lava_still" ? to(Blocks.flowing_lava).icon[0] : (p_149803_0_ == "lava_flow" ? to(Blocks.flowing_lava).icon[1] : null)));
    }
}