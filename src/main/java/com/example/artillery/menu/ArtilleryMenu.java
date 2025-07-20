package com.example.artillery.menu;

import com.example.artillery.blockentity.GunBlockEntity;
import com.example.artillery.registry.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

public final class ArtilleryMenu extends AbstractContainerMenu {

    private final GunBlockEntity gun;

    public ArtilleryMenu(int id, Inventory inv, GunBlockEntity gun) {
        super(ModMenus.ARTILLERY_MENU, id);
        this.gun = gun;
    }

    public GunBlockEntity getGun() { return gun; }

    @Override
    public boolean stillValid(Player player) {
        if (gun == null) return false;
        return player.level() == gun.getLevel() &&
               player.distanceToSqr(
                    gun.getBlockPos().getX() + 0.5,
                    gun.getBlockPos().getY() + 0.5,
                    gun.getBlockPos().getZ() + 0.5) <= 64.0;
    }
}
