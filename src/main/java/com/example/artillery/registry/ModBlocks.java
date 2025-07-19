package com.example.artillery.registry;

import com.example.artillery.ArtilleryMod;
import com.example.artillery.block.GunBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    private ModBlocks(){}
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, ArtilleryMod.MODID);

    public static final RegistryObject<Block> GUN_BLOCK = BLOCKS.register("gun_block", () ->
        new GunBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0f, 1200.0f).noOcclusion()));
}
