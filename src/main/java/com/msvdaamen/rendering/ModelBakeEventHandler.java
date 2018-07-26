package com.msvdaamen.rendering;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModelBakeEventHandler {
    public static final ModelBakeEventHandler instance = new ModelBakeEventHandler();

    private ModelBakeEventHandler() {};

    // Called after all the other baked block models have been added to the modelRegistry
    // Allows us to manipulate the modelRegistry before BlockModelShapes caches them.
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event)
    {
        // Find the existing mapping for CamouflageBakedModel - it will have been added automatically because
        //  we registered a custom BlockStateMapper for it (using ModelLoader.setCustomStateMapper)
        // Replace the mapping with our CamouflageBakedModel.
        Object object =  event.getModelRegistry().getObject(DrawbridgeBakedModel.variantTag);
        if (object instanceof IBakedModel) {
            IBakedModel existingModel = (IBakedModel)object;
            DrawbridgeBakedModel customModel = new DrawbridgeBakedModel(existingModel);
            event.getModelRegistry().putObject(DrawbridgeBakedModel.variantTag, customModel);
        }
    }
}
