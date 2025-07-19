package com.example.artillery.registry;

import com.example.artillery.ArtilleryMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    private ModItems(){}
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, ArtilleryMod.MODID);

    public static final RegistryObject<Item> GUN_BLOCK_ITEM =
        ITEMS.register("gun_block", () -> new BlockItem(ModBlocks.GUN_BLOCK.get(), new Item.Properties()));
}
