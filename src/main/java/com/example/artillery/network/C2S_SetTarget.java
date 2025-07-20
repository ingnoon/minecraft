package com.example.artillery.network;

import com.example.artillery.blockentity.GunBlockEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.level.ServerPlayer;

public record C2S_SetTarget(BlockPos gunPos, BlockPos targetPos) {
    public static void send(BlockPos gunPos, BlockPos targetPos) {
        var buf = PacketByteBufs.create();
        buf.writeBlockPos(gunPos);
        buf.writeBlockPos(targetPos);
        ClientPlayNetworking.send(NetworkHandler.SET_TARGET, buf);
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
        BlockPos gunPos = buf.readBlockPos();
        BlockPos target = buf.readBlockPos();
        server.execute(() -> {
            if (player.level().getBlockEntity(gunPos) instanceof GunBlockEntity gun) {
                gun.setTarget(target);
            }
        });
    }
}
