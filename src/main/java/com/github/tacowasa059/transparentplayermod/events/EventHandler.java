package com.github.tacowasa059.transparentplayermod.events;

import com.github.tacowasa059.transparentplayermod.TransparentPlayerMod;
import com.github.tacowasa059.transparentplayermod.capabilities.provider.AlphaValueProvider;
import com.github.tacowasa059.transparentplayermod.utils.ModUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TransparentPlayerMod.MOD_ID)
public class EventHandler {
    @SubscribeEvent//情報の同期
    public static void onJoinEvent(PlayerEvent.PlayerLoggedInEvent event){
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) return; // サーバーサイドのみで処理を行う
        ServerPlayerEntity loggedInPlayer = (ServerPlayerEntity) event.getPlayer();
        MinecraftServer server = loggedInPlayer.getServer(); // イベントが発生したプレイヤーからサーバーインスタンスを取得
        if (server == null) return;
        PlayerEntity playerEntity=event.getPlayer();
        ModUtils.sendAlltoOneClient(server, (ServerPlayerEntity) playerEntity);
        loggedInPlayer.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue -> {
            ModUtils.sendOnetoAllClient(loggedInPlayer.getUniqueID(),alphaValue.getAlpha());
        });
    }
}
