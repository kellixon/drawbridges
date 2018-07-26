package com.msvdaamen.proxy;

import com.msvdaamen.drawbridges.ModOredict;
import com.msvdaamen.init.Config;
import com.msvdaamen.init.ModBlocks;
import com.msvdaamen.init.ModItems;
import com.msvdaamen.inventory.GuiProxy;
import com.msvdaamen.network.PacketHandler;
import com.msvdaamen.world.OreGen;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

import static com.msvdaamen.drawbridges.Drawbridges.instance;

@Mod.EventBusSubscriber
public class CommonProxy {

    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
        ModBlocks.registerDrawbridges();
        ModItems.registerDrawbridge();
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "drawbridges.cfg"));
        Config.readConfig();
        PacketHandler.registerMessages("Drawbridges");
    }

    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
        if(!Config.useVanillaRecipe) {
            GameRegistry.registerWorldGenerator(new OreGen(), 0);
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.registerBlocks(event);
        ModBlocks.registerTileEntity();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModBlocks.registerItemBlock(event);
        ModItems.registerItems(event);
        ModOredict.registerOredict();
    }
}