package com.example.artillery.registry;

import com.example.artillery.ArtilleryMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Registry;

public final class ModCreativeTabs {
    private ModCreativeTabs(){}

    public static CreativeModeTab ARTILLERY_TAB;

    public static void register() {
        ARTILLERY_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(ArtilleryMod.MODID, "artillery_tab"),
            FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.artillery"))
                .icon(() -> new ItemStack(ModBlocks.GUN_BLOCK))
                .displayItems((params, output) -> output.accept(ModBlocks.GUN_BLOCK))
                .build());
    }
}
