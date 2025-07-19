package com.example.artillery.client.screen;

import com.example.artillery.menu.ArtilleryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class ReconScreen extends AbstractContainerScreen<ArtilleryMenu> {
    public ReconScreen(ArtilleryMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 90;
    }
    @Override protected void renderBg(GuiGraphics gfx, float pt, int mx, int my) { }
    @Override public void render(GuiGraphics gfx, int mx, int my, float pt){
        renderBackground(gfx);
        super.render(gfx,mx,my,pt);
        drawString(gfx, font, "Recon TODO", leftPos+10, topPos+10, 0xFFFFFF);
    }
}
