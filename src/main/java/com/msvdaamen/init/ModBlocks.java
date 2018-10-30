package com.msvdaamen.init;

import com.msvdaamen.blocks.*;
import com.msvdaamen.drawbridges.Drawbridges;
import com.msvdaamen.tileentities.TileEntityAdvDrawbridge;
import com.msvdaamen.tileentities.TileEntityDrawbridge;
import com.msvdaamen.tileentities.TileEntityPulseDrawbridge;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class ModBlocks {
    private static ArrayList<baseBlock> blocks = new ArrayList<>();

    @GameRegistry.ObjectHolder(Drawbridges.MODID + ":drawbridge")
    public static Drawbridge drawbridge;

    @GameRegistry.ObjectHolder(Drawbridges.MODID + ":extendeddrawbridge")
    public static ExtendedDrawbridge extendeddrawbridge;

    @GameRegistry.ObjectHolder(Drawbridges.MODID + ":advanceddrawbridge")
    public static AdvDrawbridge advanceddrawbridge;

    @GameRegistry.ObjectHolder(Drawbridges.MODID + ":oreCopper")
    public static OreBlock oreCopper;

    @GameRegistry.ObjectHolder(Drawbridges.MODID + ":oreTin")
    public static OreBlock oreTin;

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if(!Config.useVanillaRecipe) {
            event.getRegistry().register(new OreBlock("Copper"));
            event.getRegistry().register(new OreBlock("Tin"));
        }
        event.getRegistry().register(new Drawbridge());
        event.getRegistry().register(new ExtendedDrawbridge());
        event.getRegistry().register(new AdvDrawbridge());
    }

    public static void registerTileEntity() {
        GameRegistry.registerTileEntity(TileEntityDrawbridge.class, new ResourceLocation(Drawbridges.MODID + "_drawbridge"));
        GameRegistry.registerTileEntity(TileEntityPulseDrawbridge.class, new ResourceLocation(Drawbridges.MODID + "_pulsedrawbridge"));
        GameRegistry.registerTileEntity(TileEntityAdvDrawbridge.class, new ResourceLocation(Drawbridges.MODID + "_advdrawbridge"));
    }

    public static void addBlocks(baseBlock block) {
        blocks.add(block);
    }

    public static void registerItemBlock(RegistryEvent.Register<Item> event) {
        for(Block block: blocks) {
            if(block instanceof  OreBlock) {
                if(!Config.useVanillaRecipe) {
                    event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
                }
            } else {
                event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        for(baseBlock block: blocks) {
            if(block instanceof  OreBlock) {
                if(!Config.useVanillaRecipe) {
                    block.initModel();
                }
            } else {
                block.initModel();
            }
        }
    }
}
