package com.github.tacowasa059.transparentplayermod.packet.packets;

import com.github.tacowasa059.transparentplayermod.capabilities.provider.AlphaValueProvider;
import com.github.tacowasa059.transparentplayermod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class sendCommandUpdatePacket {
    private final UUID playerUUID;
    private final int alphaValue;

    public sendCommandUpdatePacket(UUID playerUUID, int alphaValue) {
        this.playerUUID = playerUUID;
        this.alphaValue = alphaValue;
    }

    public static void encode(sendCommandUpdatePacket msg, PacketBuffer buf) {
        buf.writeUniqueId(msg.playerUUID);
        buf.writeInt(msg.alphaValue);
    }

    public static sendCommandUpdatePacket decode(PacketBuffer buf) {
        return new sendCommandUpdatePacket(buf.readUniqueId(), buf.readInt());
    }

    public static void handle(sendCommandUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if(server==null)return;
            PlayerEntity player = server.getPlayerList().getPlayerByUUID(msg.playerUUID);
            int alpha= msg.alphaValue;
            if (player != null) {
                player.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue1 -> {
                    alphaValue1.setAlpha(alpha);
                    ModUtils.sendOnetoAllClient(server, msg.playerUUID, msg.alphaValue);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
