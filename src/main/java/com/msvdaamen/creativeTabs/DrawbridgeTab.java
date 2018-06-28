package com.msvdaamen.creativeTabs;

import com.msvdaamen.blocks.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class DrawbridgeTab extends CreativeTabs {

    public DrawbridgeTab(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModBlocks.drawbridge);
    }
}
