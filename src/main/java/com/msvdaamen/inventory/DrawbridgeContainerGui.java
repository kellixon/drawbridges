package com.msvdaamen.inventory;

import com.msvdaamen.drawbridges.Drawbridges;
import com.msvdaamen.tileentities.TileEntityDrawbridge;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class DrawbridgeContainerGui extends GuiContainer {

    public static final int WIDTH = 176;
    public static final int HEIGHT = 166;
    private TileEntityDrawbridge te;

    private static final ResourceLocation DRAWBRIDGE_GUI_TEXTURE = new ResourceLocation(Drawbridges.MODID, "textures/gui/drawbridge.png");

    public DrawbridgeContainerGui(TileEntityDrawbridge te, DrawbridgeContainer container) {
        super(container);
        xSize = WIDTH;
        ySize = HEIGHT;
        this.te = te;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = "Drawbridge";
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(DRAWBRIDGE_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}
