package com.msvdaamen.blocks;


import com.msvdaamen.drawbridges.Drawbridges;
import com.msvdaamen.init.ModBlocks;
import com.msvdaamen.init.ModItems;
import com.msvdaamen.inventory.GuiProxy;
import com.msvdaamen.rendering.DrawbridgeBakedModel;
import com.msvdaamen.tileentities.TileEntityDrawbridge;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Drawbridge extends BlockFacing implements ITileEntityProvider {

    public Drawbridge() {
        super();
        setUnlocalizedName(Drawbridges.MODID + ".drawbridge");
        setRegistryName("drawbridge");
        ModBlocks.addBlocks(this);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityDrawbridge(16, 10);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if(isTileEntity(world, pos)) {
            TileEntityDrawbridge tile = (TileEntityDrawbridge) world.getTileEntity(pos);
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

    public boolean isTileEntity (IBlockAccess world, BlockPos pos) {
        if(world.getTileEntity(pos) != null) {
            if(world.getTileEntity(pos) instanceof TileEntityDrawbridge) {
                return true;
            }
        }
        return false;
    }



    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (world.isRemote) {
            return true;
        }
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityDrawbridge)) {
            return false;
        }
        player.openGui(Drawbridges.instance, GuiProxy.DRAWBRIDGE, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
