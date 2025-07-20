package com.example.artillery.network;

import com.example.artillery.blockentity.GunBlockEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;

public record C2S_Fire(BlockPos gunPos, boolean highAngle) {
    public static void send(BlockPos pos, boolean high) {
        var buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeBoolean(high);
        ClientPlayNetworking.send(NetworkHandler.FIRE, buf);
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        boolean high = buf.readBoolean();
        server.execute(() -> {
            if (player.level().getBlockEntity(pos) instanceof GunBlockEntity gun) {
                gun.tryFire(player, high);
            }
        });
    }
}
