package com.example.artillery.client;

import com.example.artillery.client.screen.ArtilleryScreen;
import com.example.artillery.menu.ArtilleryMenu;
import com.example.artillery.registry.ModMenus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterMenuScreensEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientInit {
    private ClientInit(){}

    @SubscribeEvent
    public static void onRegisterMenus(RegisterMenuScreensEvent evt) {
        evt.register(ModMenus.ARTILLERY_MENU.get(), ArtilleryScreen::new);
    }
}
