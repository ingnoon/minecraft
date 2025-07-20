package com.example.artillery.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketByteBuf;


/**
 * angleLow/High 및 각 비행시간 전달.
 * 값이 없으면 NaN.
 */
public record S2C_Solution(BlockPos gunPos, float angleLow, float angleHigh, float tofLow, float tofHigh) {
    public static void encode(S2C_Solution msg, PacketByteBuf buf) {
        buf.writeBlockPos(msg.gunPos);
        buf.writeFloat(msg.angleLow);
        buf.writeFloat(msg.angleHigh);
        buf.writeFloat(msg.tofLow);
        buf.writeFloat(msg.tofHigh);
    }
    public static S2C_Solution decode(PacketByteBuf buf) {
        return new S2C_Solution(buf.readBlockPos(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }
}
