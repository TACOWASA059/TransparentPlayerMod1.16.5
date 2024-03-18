package com.github.tacowasa059.transparentplayermod.utils;

import com.github.tacowasa059.transparentplayermod.capabilities.provider.AlphaValueProvider;
import com.github.tacowasa059.transparentplayermod.packet.PacketHandler;
import com.github.tacowasa059.transparentplayermod.packet.packets.UpdateAlphaPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;

public class ModUtils {
    public static void sendAlltoOneClient(MinecraftServer server, ServerPlayerEntity playerEntity) {
        // すべてのプレイヤーを取得してループ処理
        for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
            UUID uuid=player.getUniqueID();
            player.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue -> {
                int value=alphaValue.getAlpha();
                UpdateAlphaPacket packet=new UpdateAlphaPacket(uuid,value);
                PacketHandler.channel.send(PacketDistributor.PLAYER.with(() -> playerEntity), packet);
            });
        }
    }
    public static void sendOnetoAllClient(MinecraftServer server, UUID uuid,int alpha){
        UpdateAlphaPacket packet= new UpdateAlphaPacket(uuid,alpha);
        for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
            PacketHandler.channel.send(PacketDistributor.PLAYER.with(() -> player), packet);
        }
    }
}
