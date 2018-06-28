package com.msvdaamen.proxy;

import com.msvdaamen.blocks.Drawbridge;
import com.msvdaamen.blocks.ModBlocks;
import com.msvdaamen.drawbridges.Drawbridges;
import com.msvdaamen.inventory.GuiProxy;
import com.msvdaamen.tileentities.TileEntityDrawbridge;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.msvdaamen.drawbridges.Drawbridges.instance;

@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
    }

    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new Drawbridge());
        GameRegistry.registerTileEntity(TileEntityDrawbridge.class, new ResourceLocation(Drawbridges.MODID + "_drawbridge"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(ModBlocks.drawbridge).setRegistryName(ModBlocks.drawbridge.getRegistryName()));
    }
}