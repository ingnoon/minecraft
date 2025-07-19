package com.example.artillery.network;

import com.example.artillery.blockentity.GunBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public record C2S_SetMuzzleVel(BlockPos gunPos, int muzzleVel) {
    public static void encode(C2S_SetMuzzleVel msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.gunPos);
        buf.writeVarInt(msg.muzzleVel);
    }
    public static C2S_SetMuzzleVel decode(FriendlyByteBuf buf) {
        return new C2S_SetMuzzleVel(buf.readBlockPos(), buf.readVarInt());
    }
    public static void handle(C2S_SetMuzzleVel msg, Supplier<NetworkEvent.Context> ctxSup){
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer ply = ctx.getSender(); if (ply==null) return;
            if (ply.level().getBlockEntity(msg.gunPos) instanceof GunBlockEntity gun) {
                gun.setMuzzleVel(msg.muzzleVel);
            }
        });
        ctx.setPacketHandled(true);
    }
}
