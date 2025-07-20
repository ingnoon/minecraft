package com.example.artillery.network;

import com.example.artillery.ArtilleryMod;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public final class NetworkHandler {
    private NetworkHandler(){}

    public static final ResourceLocation SET_TARGET = new ResourceLocation(ArtilleryMod.MODID, "set_target");
    public static final ResourceLocation SET_MUZZLE = new ResourceLocation(ArtilleryMod.MODID, "set_muzzle");
    public static final ResourceLocation REQUEST_SOLVE = new ResourceLocation(ArtilleryMod.MODID, "request_solve");
    public static final ResourceLocation FIRE = new ResourceLocation(ArtilleryMod.MODID, "fire");
    public static final ResourceLocation SOLUTION = new ResourceLocation(ArtilleryMod.MODID, "solution");
    public static final ResourceLocation GUN_STATE = new ResourceLocation(ArtilleryMod.MODID, "gun_state");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(SET_TARGET, C2S_SetTarget::handle);
        ServerPlayNetworking.registerGlobalReceiver(SET_MUZZLE, C2S_SetMuzzleVel::handle);
        ServerPlayNetworking.registerGlobalReceiver(REQUEST_SOLVE, C2S_RequestSolve::handle);
        ServerPlayNetworking.registerGlobalReceiver(FIRE, C2S_Fire::handle);
    }
}
