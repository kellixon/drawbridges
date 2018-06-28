package com.msvdaamen.models;

import com.msvdaamen.drawbridges.Drawbridges;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import javax.annotation.Nullable;
import java.util.List;

public class DrawbridgeBakedModel implements IBakedModel {

    public static final ModelResourceLocation variantTag
            = new ModelResourceLocation(Drawbridges.MODID + ":drawbridge");

    private IBakedModel normalModel;

    public DrawbridgeBakedModel(IBakedModel normalModel) {
        this.normalModel = normalModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return handleBlockState(state).getQuads(state, side, rand);
    }

    private IBakedModel handleBlockState(@Nullable IBlockState iBlockState)
    {
        Minecraft mc = Minecraft.getMinecraft();
        BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
        BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();
        IBakedModel copiedBlockModel = blockModelShapes.getModelForState(Blocks.DIAMOND_BLOCK.getDefaultState());
        return copiedBlockModel;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return normalModel.getParticleTexture();
    }

    // ideally, this should be changed for different blocks being camouflaged, but this is not supported by vanilla
    @Override
    public boolean isAmbientOcclusion()
    {
        return normalModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return normalModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return normalModel.isBuiltInRenderer();
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return normalModel.getOverrides();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return normalModel.getItemCameraTransforms();
    }
}
