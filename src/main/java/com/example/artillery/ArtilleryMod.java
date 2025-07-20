package com.example.artillery;

import com.example.artillery.registry.*;
import com.example.artillery.network.NetworkHandler;
import net.fabricmc.api.ModInitializer;

public final class ArtilleryMod implements ModInitializer {
    public static final String MODID = "artillery";

    @Override
    public void onInitialize() {
        ArtilleryConfig.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModMenus.register();
        ModCreativeTabs.register();
        ModEntities.register();
        ModItems.register();

        NetworkHandler.init();
    }
}
