package com.example.artillery.registry;

import com.example.artillery.ArtilleryMod;
import com.example.artillery.block.GunBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.core.Registry;

public final class ModBlocks {
    private ModBlocks(){}

    public static Block GUN_BLOCK;

    public static void register() {
        GUN_BLOCK = Registry.register(BuiltInRegistries.BLOCK,
            new ResourceLocation(ArtilleryMod.MODID, "gun_block"),
            new GunBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0f, 1200.0f).noOcclusion()));
    }
}
