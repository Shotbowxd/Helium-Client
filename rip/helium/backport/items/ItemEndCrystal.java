package rip.helium.backport.items;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

/* 
 * 
 * Author: MichaelMaymays 
 * 
 * Currently not working, as I first need to figure out how to
 * make it compatible with ViaVersion.
 * 
 */

public class ItemEndCrystal extends Item
{
    public ItemEndCrystal()
    {
        this.setUnlocalizedName("end_crystal");
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() != Blocks.obsidian && iblockstate.getBlock() != Blocks.bedrock)
        {
            return false;
        }
        else
        {
            BlockPos blockpos = pos.up();
            ItemStack itemstack = playerIn.getHeldItem();

            if (!stack.canEditBlocks())
            {
                return false;
            }
            else
            {
                BlockPos blockpos1 = blockpos.up();
                boolean flag = !(worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.air) && !worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
                flag = flag | (!(worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.air) && !worldIn.getBlockState(blockpos1).getBlock().isReplaceable(worldIn, blockpos1));

                if (flag)
                {
                    return false;
                }
                else
                {
                    double d0 = (double)blockpos.getX();
                    double d1 = (double)blockpos.getY();
                    double d2 = (double)blockpos.getZ();
                    List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));

                    if (!list.isEmpty())
                    {
                        return false;
                    }
                    else
                    {
                        if (!worldIn.isRemote)
                        {
                            EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(worldIn, (double)((float)pos.getX() + 0.5F), (double)(pos.getY() + 1), (double)((float)pos.getZ() + 0.5F));
                            worldIn.spawnEntityInWorld(entityendercrystal);
                        }

                        //itemstack.func_190918_g(1);
                        return true;
                    }
                }
            }
        }
    }

    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}
