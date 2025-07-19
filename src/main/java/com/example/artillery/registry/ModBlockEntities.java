package com.example.artillery.registry;

import com.example.artillery.ArtilleryMod;
import com.example.artillery.blockentity.GunBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {
    private ModBlockEntities(){}
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArtilleryMod.MODID);

    public static final RegistryObject<BlockEntityType<GunBlockEntity>> GUN_BLOCK_ENTITY =
        BLOCK_ENTITIES.register("gun_block_entity",
            () -> BlockEntityType.Builder.of(GunBlockEntity::new, ModBlocks.GUN_BLOCK.get()).build(null));
}
