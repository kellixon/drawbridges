package com.msvdaamen.inventory;

import com.msvdaamen.tileentities.TileEntityDrawbridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityDrawbridge) {
            return new DrawbridgeContainer(player.inventory, (TileEntityDrawbridge) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityDrawbridge) {
            TileEntityDrawbridge tileEntityDrawbridge = (TileEntityDrawbridge) te;
            return new DrawbridgeContainerGui(tileEntityDrawbridge, new DrawbridgeContainer(player.inventory, tileEntityDrawbridge));
        }
        return null;
    }
}
