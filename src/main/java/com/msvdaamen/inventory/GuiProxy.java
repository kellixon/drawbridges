package com.msvdaamen.inventory;

import com.msvdaamen.tileentities.TileEntityAdvDrawbridge;
import com.msvdaamen.tileentities.TileEntityDrawbridge;
import com.msvdaamen.tileentities.TileEntityPulseDrawbridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

    public static final int DRAWBRIDGE = 1;
    public static final int EXTENDED_DRAWBRIDGE = 2;
    public static final int ADV_DRAWBRIDGE = 3;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (ID == DRAWBRIDGE) {
            return new DrawbridgeContainer(player.inventory, (TileEntityDrawbridge) te);
        }
        if(ID == EXTENDED_DRAWBRIDGE) {
            return new ExtendedDrawbridgeContainer(player.inventory, (TileEntityPulseDrawbridge) te);
        }
        if(ID == ADV_DRAWBRIDGE) {
            return new AdvDrawbridgeContainer(player.inventory, (TileEntityAdvDrawbridge) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (ID == DRAWBRIDGE) {
            TileEntityDrawbridge tileEntityDrawbridge = (TileEntityDrawbridge) te;
            return new DrawbridgeContainerGui(tileEntityDrawbridge, new DrawbridgeContainer(player.inventory, tileEntityDrawbridge));
        }
        if(ID == EXTENDED_DRAWBRIDGE) {
            TileEntityPulseDrawbridge tileEntityPulseDrawbridge = (TileEntityPulseDrawbridge) te;
            return new ExtendedDrawbridgeGui(tileEntityPulseDrawbridge, new ExtendedDrawbridgeContainer(player.inventory, tileEntityPulseDrawbridge));
        }
        if(ID == ADV_DRAWBRIDGE) {
            TileEntityAdvDrawbridge tileEntityAdvDrawbridge = (TileEntityAdvDrawbridge) te;
            return new AdvDrawbridgeGui(tileEntityAdvDrawbridge, new AdvDrawbridgeContainer(player.inventory, tileEntityAdvDrawbridge));
        }
        return null;
    }
}
