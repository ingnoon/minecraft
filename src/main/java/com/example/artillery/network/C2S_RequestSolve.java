package com.example.artillery.network;

import com.example.artillery.blockentity.GunBlockEntity;
import com.example.artillery.physics.BallisticsSolver;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.level.ServerPlayer;

public record C2S_RequestSolve(BlockPos gunPos) {
    public static void send(BlockPos pos) {
        var buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        ClientPlayNetworking.send(NetworkHandler.REQUEST_SOLVE, buf);
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        server.execute(() -> {
            if (player.level().getBlockEntity(pos) instanceof GunBlockEntity gun) {
                gun.solveAndCache();
                var pair = gun.getCachedPair();
                if (pair != null) {
                    float low = pair.low()!=null ? (float)pair.low().angleRad() : Float.NaN;
                    float high = pair.high()!=null ? (float)pair.high().angleRad() : Float.NaN;
                    float tofLow = pair.low()!=null ? (float)pair.low().flightTime() : Float.NaN;
                    float tofHigh = pair.high()!=null ? (float)pair.high().flightTime() : Float.NaN;
                    var out = PacketByteBufs.create();
                    out.writeBlockPos(pos);
                    out.writeFloat(low); out.writeFloat(high);
                    out.writeFloat(tofLow); out.writeFloat(tofHigh);
                    ServerPlayNetworking.send(player, NetworkHandler.SOLUTION, out);
                }
            }
        });
    }
}
