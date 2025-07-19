package com.example.artillery.network;

import com.example.artillery.blockentity.GunBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record C2S_SetTarget(BlockPos gunPos, BlockPos targetPos) {
    public static void encode(C2S_SetTarget msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.gunPos);
        buf.writeBlockPos(msg.targetPos);
    }
    public static C2S_SetTarget decode(FriendlyByteBuf buf) {
        return new C2S_SetTarget(buf.readBlockPos(), buf.readBlockPos());
    }
    public static void handle(C2S_SetTarget msg, Supplier<NetworkEvent.Context> ctxSup){
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer ply = ctx.getSender(); if (ply==null) return;
            if (ply.level().getBlockEntity(msg.gunPos) instanceof GunBlockEntity gun) {
                gun.setTarget(msg.targetPos);
            }
        });
        ctx.setPacketHandled(true);
    }
}
