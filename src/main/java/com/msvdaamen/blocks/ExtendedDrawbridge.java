package com.msvdaamen.blocks;

import com.msvdaamen.drawbridges.Drawbridges;
import com.msvdaamen.init.ModBlocks;
import com.msvdaamen.inventory.GuiProxy;
import com.msvdaamen.tileentities.TileEntityDrawbridge;
import com.msvdaamen.tileentities.TileEntityPulseDrawbridge;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ExtendedDrawbridge extends BlockFacing implements ITileEntityProvider {

    private boolean active;

    public ExtendedDrawbridge() {
        setRegistryName("extendeddrawbridge");
        setUnlocalizedName(Drawbridges.MODID + ".extendeddrawbridge");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityPulseDrawbridge(64, 10);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (world.isRemote) {
            return true;
        }
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityPulseDrawbridge)) {
            return false;
        }
        player.openGui(Drawbridges.instance, GuiProxy.EXTENDED_DRAWBRIDGE, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void observedNeighborChange(IBlockState observerState, World world, BlockPos observerPos, Block changedBlock, BlockPos changedBlockPos) {
        boolean changed = false;
        if(isTileEntity(world, observerPos) && !changed) {
            TileEntityPulseDrawbridge tile = (TileEntityPulseDrawbridge)world.getTileEntity(observerPos);
            if(world.isBlockPowered(changedBlockPos)) {
                if(tile.getActive() && !active) {
                    active = true;
                    tile.setActive(false);
                }else if(!active) {
                    active = true;
                    tile.setActive(true);
                }

            } else {
                active = false;
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if(isTileEntity(world, pos)) {
            TileEntityPulseDrawbridge tile = (TileEntityPulseDrawbridge) world.getTileEntity(pos);
            ItemStack stack1 = tile.itemStackHandler.getStackInSlot(tile.MAINSLOT);
            ItemStack stack2 = tile.itemStackHandler.getStackInSlot(tile.CAMMOSLOT);
            if(!stack1.isEmpty()) {
                EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack1);
                world.spawnEntity(item);
            }
            if(!stack2.isEmpty()) {
                EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack2);
                world.spawnEntity(item);
            }
        }
    }

    public boolean isTileEntity (World world, BlockPos pos) {
        if(world.getTileEntity(pos) != null) {
            if(world.getTileEntity(pos) instanceof TileEntityPulseDrawbridge) {
                return true;
            }
        }
        return false;
    }
}
