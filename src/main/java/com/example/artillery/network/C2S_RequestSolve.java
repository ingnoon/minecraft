package com.example.artillery.network;

import com.example.artillery.blockentity.GunBlockEntity;
import com.example.artillery.physics.BallisticsSolver;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import java.util.function.Supplier;

public record C2S_RequestSolve(BlockPos gunPos) {
    public static void encode(C2S_RequestSolve msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.gunPos);
    }
    public static C2S_RequestSolve decode(FriendlyByteBuf buf) {
        return new C2S_RequestSolve(buf.readBlockPos());
    }
    public static void handle(C2S_RequestSolve msg, Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer ply = ctx.getSender(); if (ply == null) return;
            if (ply.level().getBlockEntity(msg.gunPos) instanceof GunBlockEntity gun) {
                gun.solveAndCache();
                var pair = gun.getCachedPair();
                if (pair != null) {
                    float low = pair.low()!=null ? (float)pair.low().angleRad() : Float.NaN;
                    float high = pair.high()!=null ? (float)pair.high().angleRad() : Float.NaN;
                    float tofLow = pair.low()!=null ? (float)pair.low().flightTime() : Float.NaN;
                    float tofHigh = pair.high()!=null ? (float)pair.high().flightTime() : Float.NaN;
                    NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> ply),
                        new S2C_Solution(msg.gunPos, low, high, tofLow, tofHigh));
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
