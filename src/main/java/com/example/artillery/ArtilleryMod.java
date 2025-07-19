package com.example.artillery;

import com.example.artillery.registry.*;
import com.example.artillery.network.NetworkHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod(ArtilleryMod.MODID)
public final class ArtilleryMod {
    public static final String MODID = "artillery";

    public ArtilleryMod() {
        ArtilleryConfig.register();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        ModMenus.MENUS.register(bus);
        ModCreativeTabs.CREATIVE_TABS.register(bus);
        ModEntities.ENTITIES.register(bus);
        ModItems.ITEMS.register(bus);

        NetworkHandler.init();
    }
}
