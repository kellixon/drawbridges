package com.msvdaamen.blocks;


import com.msvdaamen.drawbridges.Drawbridges;
import com.msvdaamen.init.Config;
import com.msvdaamen.init.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class OreBlock extends baseBlock {

    public OreBlock(String name) {
        setRegistryName("ore" + name);
        setUnlocalizedName(Drawbridges.MODID + '.' + "ore" + name);
        ModBlocks.addBlocks(this);
    }
}
