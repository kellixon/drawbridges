package com.msvdaamen.inventory;

import com.msvdaamen.drawbridges.Drawbridges;
import com.msvdaamen.network.PacketHandler;
import com.msvdaamen.network.PacketModeToggle;
import com.msvdaamen.tileentities.TileEntityAdvDrawbridge;
import com.msvdaamen.tileentities.TileEntityPulseDrawbridge;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class AdvDrawbridgeGui extends GuiContainer {

    public static final int WIDTH = 176;
    public static final int HEIGHT = 166;
    private TileEntityAdvDrawbridge te;
    private static final ResourceLocation DRAWBRIDGE_GUI_TEXTURE = new ResourceLocation(Drawbridges.MODID, "textures/gui/advdrawbridge.png");

    public AdvDrawbridgeGui(TileEntityAdvDrawbridge te, AdvDrawbridgeContainer container) {
        super(container);
        xSize = WIDTH;
        ySize = HEIGHT;
        this.te = te;
    }

    @Override
    public void initGui() {
        super.initGui();
        String name;
        if(te.getMode()) {
            name = "Redstone mode";
        }else {
            name = "Pulse mode";
        }
        GuiButton button = new GuiButton(1, (guiLeft + WIDTH / 2) - 10, guiTop + 17, name);
        button.setWidth(90);
        addButton(button);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = "Extended Drawbridge";
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

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 1) {
            if(te.getMode()) {
                te.setMode(false);
                button.displayString = "Pulse mode";
                PacketHandler.INSTANCE.sendToServer(new PacketModeToggle(false, te.getPos()));
            }else {
                te.setMode(true);
                button.displayString = "Redstone mode";
                PacketHandler.INSTANCE.sendToServer(new PacketModeToggle(true, te.getPos()));
            }
        }
    }
}
