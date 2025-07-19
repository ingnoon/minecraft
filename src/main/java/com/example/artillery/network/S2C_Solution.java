package com.example.artillery.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

/**
 * angleLow/High 및 각 비행시간 전달.
 * 값이 없으면 NaN.
 */
public record S2C_Solution(BlockPos gunPos, float angleLow, float angleHigh, float tofLow, float tofHigh) {
    public static void encode(S2C_Solution msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.gunPos);
        buf.writeFloat(msg.angleLow);
        buf.writeFloat(msg.angleHigh);
        buf.writeFloat(msg.tofLow);
        buf.writeFloat(msg.tofHigh);
    }
    public static S2C_Solution decode(FriendlyByteBuf buf) {
        return new S2C_Solution(buf.readBlockPos(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }
    public static void handle(S2C_Solution msg, Supplier<NetworkEvent.Context> ctxSup) {
        // client stores last solution; handled in screen
        ctxSup.get().setPacketHandled(true);
    }
}
