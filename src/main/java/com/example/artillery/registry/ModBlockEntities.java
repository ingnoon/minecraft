package com.example.artillery.registry;

import com.example.artillery.ArtilleryMod;
import com.example.artillery.blockentity.GunBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {
    private ModBlockEntities(){}

    public static BlockEntityType<GunBlockEntity> GUN_BLOCK_ENTITY;

    public static void register() {
        GUN_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(ArtilleryMod.MODID, "gun_block_entity"),
            BlockEntityType.Builder.of(GunBlockEntity::new, ModBlocks.GUN_BLOCK).build(null));
    }
}
