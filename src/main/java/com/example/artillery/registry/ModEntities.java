package com.example.artillery.registry;

import com.example.artillery.ArtilleryMod;
import com.example.artillery.entity.ArtilleryShellEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {
    private ModEntities(){}

    public static EntityType<ArtilleryShellEntity> ARTILLERY_SHELL;

    public static void register() {
        ARTILLERY_SHELL = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ArtilleryMod.MODID, "artillery_shell"),
            EntityType.Builder.<ArtilleryShellEntity>of(ArtilleryShellEntity::new, MobCategory.MISC)
                .sized(0.25f,0.25f)
                .updateInterval(1)
                .build(ArtilleryMod.MODID + ":artillery_shell"));
    }
}
