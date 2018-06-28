package com.msvdaamen.blocks;

import com.msvdaamen.drawbridges.Drawbridges;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

    @GameRegistry.ObjectHolder(Drawbridges.MODID + ":drawbridge")
    public static Drawbridge drawbridge;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        drawbridge.initModel();
    }
    @SideOnly(Side.CLIENT)
    public static void initItemModel() {
        drawbridge.initItemModel();
    }
}
