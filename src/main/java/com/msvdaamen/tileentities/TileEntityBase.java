package com.msvdaamen.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityBase extends TileEntity {



    public void placeBlock(BlockPos pos, IBlockState block) {
        getWorld().setBlockState(pos, block);
    }

    public Block getBlockAtPos(BlockPos pos) {
        if(getWorld().getBlockState(pos).getBlock() != Blocks.AIR) {
            return getWorld().getBlockState(pos).getBlock();
        }else {
            return Blocks.AIR;
        }
    }

    public IBlockState getBlockStateAtPos(BlockPos pos) {
        if(getWorld().getBlockState(pos).getBlock() != Blocks.AIR) {
            return getWorld().getBlockState(pos);
        }else {
            return Blocks.AIR.getDefaultState();
        }
    }

    public ItemStack getItemstackWithMeta(IBlockState block) {
        return new ItemStack(block.getBlock(), 1, block.getBlock().getMetaFromState(block));
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }


}
