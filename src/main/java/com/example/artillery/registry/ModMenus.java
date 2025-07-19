package com.example.artillery.registry;

import com.example.artillery.ArtilleryMod;
import com.example.artillery.blockentity.GunBlockEntity;
import com.example.artillery.menu.ArtilleryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenus {
    private ModMenus(){}
    public static final DeferredRegister<MenuType<?>> MENUS =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, ArtilleryMod.MODID);

    public static final RegistryObject<MenuType<ArtilleryMenu>> ARTILLERY_MENU =
        MENUS.register("artillery_menu", () -> IForgeMenuType.create((windowId, inv, data) -> {
            // data unused (server opens with BE)
            if (data==null) return new ArtilleryMenu(windowId, inv, null);
            // client fallback
            return new ArtilleryMenu(windowId, inv, null);
        }));
}
