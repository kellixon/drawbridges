package com.msvdaamen.proxy;

import com.google.common.collect.Maps;
import com.msvdaamen.blocks.BlockFacing;
import com.msvdaamen.init.ModBlocks;
import com.msvdaamen.init.ModItems;
import com.msvdaamen.rendering.DrawbridgeBakedModel;
import com.msvdaamen.rendering.ModelBakeEventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
//        ModelLoaderRegistry.registerLoader(new BakedModelLoader());

        StateMapperBase ignoreState = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                Map< IProperty<?>, Comparable<? >> map = Maps. <IProperty<?>, Comparable<? >> newLinkedHashMap(iBlockState.getProperties());
                map.remove(BlockFacing.COPIEDBLOCK);
                map.remove(BlockFacing.HAS_CAMMO);
                return new ModelResourceLocation("drawbridges:drawbridge", getPropertyString(map));
//                return DrawbridgeBakedModel.blockStatesFileName;
            }
        };
        ModelLoader.setCustomStateMapper(ModBlocks.drawbridge, ignoreState);

        MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.instance);

//        ModBlocks.initItemModel();
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("drawbridges:drawbridge", "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        ModelLoader.setCustomModelResourceLocation(ModItems.itemBlockDrawbridge, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
//        ModBlocks.initModels();
        ModItems.initModels();
    }
}