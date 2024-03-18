package com.github.tacowasa059.transparentplayermod.events;

import com.github.tacowasa059.transparentplayermod.TransparentPlayerMod;
import com.github.tacowasa059.transparentplayermod.capabilities.entity.AlphaValue;
import com.github.tacowasa059.transparentplayermod.capabilities.entity.StorageAlphaValue;
import com.github.tacowasa059.transparentplayermod.capabilities.provider.AlphaValueProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TransparentPlayerMod.MOD_ID)
public class CapabilityHandler {
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(TransparentPlayerMod.MOD_ID, "alpha"), new AlphaValueProvider());
        }
    }
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event){
        if(event.isWasDeath()){
            event.getOriginal().getCapability(AlphaValueProvider.capability).ifPresent(oldStore->{
                event.getOriginal().getCapability(AlphaValueProvider.capability).ifPresent(newStore->{
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
}
