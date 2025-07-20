package com.example.artillery.registry;

import com.example.artillery.ArtilleryMod;
import com.example.artillery.menu.ArtilleryMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public final class ModMenus {
    private ModMenus(){}

    public static MenuType<ArtilleryMenu> ARTILLERY_MENU;

    public static void register() {
        ARTILLERY_MENU = Registry.register(BuiltInRegistries.MENU,
            new ResourceLocation(ArtilleryMod.MODID, "artillery_menu"),
            new MenuType<>((windowId, inv) -> new ArtilleryMenu(windowId, inv, null), FeatureFlags.VANILLA_SET));
    }
}
