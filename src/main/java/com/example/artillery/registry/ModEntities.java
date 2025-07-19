package com.example.artillery.registry;

import com.example.artillery.ArtilleryMod;
import com.example.artillery.entity.ArtilleryShellEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    private ModEntities(){}
    public static final DeferredRegister<EntityType<?>> ENTITIES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ArtilleryMod.MODID);

    public static final RegistryObject<EntityType<ArtilleryShellEntity>> ARTILLERY_SHELL =
        ENTITIES.register("artillery_shell",
            () -> EntityType.Builder.<ArtilleryShellEntity>of(ArtilleryShellEntity::new, MobCategory.MISC)
                .sized(0.25f,0.25f)
                .updateInterval(1)
                .build(ArtilleryMod.MODID+":artillery_shell"));
}
