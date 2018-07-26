package com.msvdaamen.blocks;

import com.msvdaamen.drawbridges.Drawbridges;
import com.msvdaamen.init.ModBlocks;
import com.msvdaamen.inventory.GuiProxy;
import com.msvdaamen.tileentities.TileEntityAdvDrawbridge;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AdvDrawbridge extends BlockFacing implements ITileEntityProvider {

    private boolean active;

    public AdvDrawbridge() {
        setUnlocalizedName(Drawbridges.MODID + ".advanceddrawbridge");
        setRegistryName("advanceddrawbridge");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityAdvDrawbridge();
    }



    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (world.isRemote) {
            return true;
        }
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityAdvDrawbridge)) {
            return false;
        }
        player.openGui(Drawbridges.instance, GuiProxy.ADV_DRAWBRIDGE, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void observedNeighborChange(IBlockState observerState, World world, BlockPos observerPos, Block changedBlock, BlockPos changedBlockPos) {
        boolean changed = false;
        if(isTileEntity(world, observerPos) && !changed) {
            TileEntityAdvDrawbridge tile = (TileEntityAdvDrawbridge)world.getTileEntity(observerPos);
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
            TileEntityAdvDrawbridge tile = (TileEntityAdvDrawbridge) world.getTileEntity(pos);
            for(int i = 0; i < tile.itemStackHandler.getSlots(); i++) {
                if(!tile.itemStackHandler.getStackInSlot(i).isEmpty()) {
                    EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), tile.itemStackHandler.getStackInSlot(i));
                    world.spawnEntity(item);
                }
            }
        }
    }

    public boolean isTileEntity (World world, BlockPos pos) {
        if(world.getTileEntity(pos) != null) {
            if(world.getTileEntity(pos) instanceof TileEntityAdvDrawbridge) {
                return true;
            }
        }
        return false;
    }
}
