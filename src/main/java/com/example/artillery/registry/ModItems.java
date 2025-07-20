package com.example.artillery.registry;

import com.example.artillery.ArtilleryMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public final class ModItems {
    private ModItems(){}

    public static Item GUN_BLOCK_ITEM;

    public static void register() {
        GUN_BLOCK_ITEM = Registry.register(BuiltInRegistries.ITEM,
            new ResourceLocation(ArtilleryMod.MODID, "gun_block"),
            new BlockItem(ModBlocks.GUN_BLOCK, new Item.Properties()));
    }
}
