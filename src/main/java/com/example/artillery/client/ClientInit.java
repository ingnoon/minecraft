package com.example.artillery.client;

import com.example.artillery.client.screen.ArtilleryScreen;
import com.example.artillery.menu.ArtilleryMenu;
import com.example.artillery.registry.ModMenus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public final class ClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(ModMenus.ARTILLERY_MENU, ArtilleryScreen::new);
    }
}
