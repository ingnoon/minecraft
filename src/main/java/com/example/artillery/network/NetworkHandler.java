package com.example.artillery.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.SimpleChannel;

public final class NetworkHandler {
    private NetworkHandler(){}

    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("artillery", "main"),
        () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals
    );

    private static int id = 0;
    private static int nextId() { return id++; }

    public static void init() {
        CHANNEL.registerMessage(nextId(), C2S_SetTarget.class, C2S_SetTarget::encode, C2S_SetTarget::decode, C2S_SetTarget::handle);
        CHANNEL.registerMessage(nextId(), C2S_SetMuzzleVel.class, C2S_SetMuzzleVel::encode, C2S_SetMuzzleVel::decode, C2S_SetMuzzleVel::handle);
        CHANNEL.registerMessage(nextId(), C2S_RequestSolve.class, C2S_RequestSolve::encode, C2S_RequestSolve::decode, C2S_RequestSolve::handle);
        CHANNEL.registerMessage(nextId(), C2S_Fire.class, C2S_Fire::encode, C2S_Fire::decode, C2S_Fire::handle);
        CHANNEL.registerMessage(nextId(), S2C_Solution.class, S2C_Solution::encode, S2C_Solution::decode, S2C_Solution::handle);
        CHANNEL.registerMessage(nextId(), S2C_GunState.class, S2C_GunState::encode, S2C_GunState::decode, S2C_GunState::handle);
    }
}
