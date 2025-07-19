package com.example.artillery.registry;

import com.example.artillery.ArtilleryMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {
    private ModCreativeTabs(){}

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArtilleryMod.MODID);

    public static final RegistryObject<CreativeModeTab> ARTILLERY_TAB =
        CREATIVE_TABS.register("artillery_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.artillery"))
            .icon(() -> new ItemStack(ModBlocks.GUN_BLOCK.get()))
            .displayItems((params, output) -> {
                output.accept(ModBlocks.GUN_BLOCK.get());
            })
            .build());
}
