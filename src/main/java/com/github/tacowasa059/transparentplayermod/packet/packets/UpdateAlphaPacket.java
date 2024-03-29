package com.github.tacowasa059.transparentplayermod.packet.packets;

import com.github.tacowasa059.transparentplayermod.capabilities.provider.AlphaValueProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

//server to client
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
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT.CLIENT, () -> () -> UpdateAlphaPacket.handlePacket(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }
    private static void handlePacket(UpdateAlphaPacket msg,Supplier<NetworkEvent.Context> ctx){
        Minecraft mc = Minecraft.getInstance();
        if (mc==null||mc.world == null) return;
        PlayerEntity player = mc.world.getPlayerByUuid(msg.playerUUID);
        if (player == null) return;
        int alpha= msg.alphaValue;
        player.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue1 -> {
            alphaValue1.setAlpha(alpha);
        });
    }
}
