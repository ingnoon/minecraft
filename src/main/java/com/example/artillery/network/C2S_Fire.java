package com.example.artillery.network;

import com.example.artillery.blockentity.GunBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record C2S_Fire(BlockPos gunPos, boolean highAngle) {
    public static void encode(C2S_Fire msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.gunPos);
        buf.writeBoolean(msg.highAngle);
    }
    public static C2S_Fire decode(FriendlyByteBuf buf) {
        return new C2S_Fire(buf.readBlockPos(), buf.readBoolean());
    }
    public static void handle(C2S_Fire msg, Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer ply = ctx.getSender(); if (ply==null) return;
            if (ply.level().getBlockEntity(msg.gunPos) instanceof GunBlockEntity gun) {
                gun.tryFire(ply, msg.highAngle);
            }
        });
        ctx.setPacketHandled(true);
    }
}
