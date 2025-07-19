package com.example.artillery.client.screen;

import com.example.artillery.blockentity.GunBlockEntity;
import com.example.artillery.client.screen.widget.NumericField;
import com.example.artillery.menu.ArtilleryMenu;
import com.example.artillery.network.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.network.PacketDistributor;

public final class ArtilleryScreen extends AbstractContainerScreen<ArtilleryMenu> {

    private NumericField targetX, targetY, targetZ, muzzleField;
    private Button btnRecon, btnSolve, btnFireHigh, btnFireLow;
    private final GunBlockEntity gun;

    public ArtilleryScreen(ArtilleryMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.gun = menu.getGun();
        this.imageWidth = 220;
        this.imageHeight = 150;
    }

    @Override
    protected void init() {
        super.init();
        int x = leftPos + 10;
        int y = topPos + 20;
        targetX = addRenderableWidget(new NumericField(x, y, 60, 20));
        targetY = addRenderableWidget(new NumericField(x, y+22, 60, 20));
        targetZ = addRenderableWidget(new NumericField(x, y+44, 60, 20));
        muzzleField = addRenderableWidget(new NumericField(x, y+66, 60, 20));

        if (gun != null && gun.getTarget()!=null){
            targetX.setIntValue(gun.getTarget().getX());
            targetY.setIntValue(gun.getTarget().getY());
            targetZ.setIntValue(gun.getTarget().getZ());
        }
        if (gun != null) muzzleField.setIntValue(gun.getMuzzleVel());

        int bx = leftPos + 100;
        btnRecon = addRenderableWidget(Button.builder(Component.translatable("button.artillery.recon"), b->onRecon()).bounds(bx, y, 100,20).build());
        btnSolve = addRenderableWidget(Button.builder(Component.translatable("button.artillery.solve"), b->onSolve()).bounds(bx, y+22, 100,20).build());
        btnFireHigh = addRenderableWidget(Button.builder(Component.translatable("button.artillery.fire_high"), b->onFire(true)).bounds(bx, y+44, 100,20).build());
        btnFireLow = addRenderableWidget(Button.builder(Component.translatable("button.artillery.fire_low"), b->onFire(false)).bounds(bx, y+66, 100,20).build());
    }

    private void onRecon(){
        // client raytrace → send as target
        var mc = getMinecraft();
        var ply = mc.player;
        if (ply==null) return;
        double reach = mc.gameMode.getPickRange();
        var hit = ply.pick(reach, 0, false);
        if (hit != null && hit.getType()==net.minecraft.world.phys.HitResult.Type.BLOCK){
            var bp = ((net.minecraft.world.phys.BlockHitResult)hit).getBlockPos();
            targetX.setIntValue(bp.getX());
            targetY.setIntValue(bp.getY());
            targetZ.setIntValue(bp.getZ());
            NetworkHandler.CHANNEL.sendToServer(new C2S_SetTarget(gun.getBlockPos(), bp));
        }
    }

    private void onSolve(){
        // push muzzle vel change if edited
        int vel = muzzleField.getIntValue(gun.getMuzzleVel());
        NetworkHandler.CHANNEL.sendToServer(new C2S_SetMuzzleVel(gun.getBlockPos(), vel));
        // push target
        NetworkHandler.CHANNEL.sendToServer(new C2S_SetTarget(gun.getBlockPos(),
            new net.minecraft.core.BlockPos(targetX.getIntValue(0), targetY.getIntValue(0), targetZ.getIntValue(0))));
        // request solve
        NetworkHandler.CHANNEL.sendToServer(new C2S_RequestSolve(gun.getBlockPos()));
    }

    private void onFire(boolean high){
        NetworkHandler.CHANNEL.sendToServer(new C2S_Fire(gun.getBlockPos(), high));
    }

    @Override
    protected void renderBg(GuiGraphics gfx, float partialTick, int mouseX, int mouseY) {}

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        renderBackground(gfx);
        super.render(gfx, mouseX, mouseY, partialTick);
        drawString(gfx, font, "Target X", leftPos + 10, topPos + 10, 0xFFFFFF);
        drawString(gfx, font, "Target Y", leftPos + 10, topPos + 32, 0xFFFFFF);
        drawString(gfx, font, "Target Z", leftPos + 10, topPos + 54, 0xFFFFFF);
        drawString(gfx, font, "Muzzle", leftPos + 10, topPos + 76, 0xFFFFFF);
    }
}
