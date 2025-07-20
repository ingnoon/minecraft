package com.example.artillery.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketByteBuf;

/**
 * Optional full Gun state sync (현재 미사용).
 */
public record S2C_GunState(BlockPos gunPos, int muzzleVel, BlockPos target, int cooldown) {
    public static void encode(S2C_GunState msg, PacketByteBuf buf) {
        buf.writeBlockPos(msg.gunPos);
        buf.writeVarInt(msg.muzzleVel);
        buf.writeBoolean(msg.target!=null);
        if (msg.target!=null) buf.writeBlockPos(msg.target);
        buf.writeVarInt(msg.cooldown);
    }
    public static S2C_GunState decode(PacketByteBuf buf) {
        BlockPos gunPos = buf.readBlockPos();
        int vel = buf.readVarInt();
        BlockPos tgt = null;
        if (buf.readBoolean()) tgt = buf.readBlockPos();
        int cd = buf.readVarInt();
        return new S2C_GunState(gunPos, vel, tgt, cd);
    }
    // no-op handler
}
