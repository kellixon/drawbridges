package com.msvdaamen.drawbridges;

import com.msvdaamen.creativeTabs.DrawbridgeTab;
import com.msvdaamen.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Drawbridges.MODID, name = Drawbridges.NAME, version = Drawbridges.VERSION)
public class Drawbridges {

    public static final String MODID = "drawbridges";
    public static final String NAME = "Drawbridges";
    public static final String VERSION = "0.1";

    public static ModChecker modChecker;

    public static CreativeTabs drawbridgeTab = new DrawbridgeTab(CreativeTabs.getNextID(), "Drawbridges");

    @SidedProxy(clientSide = "com.msvdaamen.proxy.ClientProxy", serverSide = "com.msvdaamen.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Drawbridges instance;

    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        modChecker = new ModChecker();
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
