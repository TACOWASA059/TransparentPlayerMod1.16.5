package com.github.tacowasa059.transparentplayermod.packet.packets;

import com.github.tacowasa059.transparentplayermod.capabilities.provider.AlphaValueProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class UpdateAlphaPacket {
    private final UUID playerUUID;
    private final int alphaValue;

    public UpdateAlphaPacket(UUID playerUUID, int alphaValue) {
        this.playerUUID = playerUUID;
        this.alphaValue = alphaValue;
    }

    public static void encode(UpdateAlphaPacket msg, PacketBuffer buf) {
        buf.writeUniqueId(msg.playerUUID);
        buf.writeInt(msg.alphaValue);
    }

    public static UpdateAlphaPacket decode(PacketBuffer buf) {
        return new UpdateAlphaPacket(buf.readUniqueId(), buf.readInt());
    }

    public static void handle(UpdateAlphaPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.world != null) {
                PlayerEntity player = mc.world.getPlayerByUuid(msg.playerUUID);
                int alpha= msg.alphaValue;
                if (player != null) {
                    player.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue1 -> {
                        alphaValue1.setAlpha(alpha);
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
