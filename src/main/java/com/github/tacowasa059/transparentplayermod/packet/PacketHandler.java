package com.github.tacowasa059.transparentplayermod.packet;

import com.github.tacowasa059.transparentplayermod.TransparentPlayerMod;
import com.github.tacowasa059.transparentplayermod.packet.packets.UpdateAlphaPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static SimpleChannel channel =
            NetworkRegistry.newSimpleChannel(
                    new ResourceLocation(TransparentPlayerMod.MOD_ID, "main"), //"main"
                    () -> PROTOCOL_VERSION,
                    PROTOCOL_VERSION::equals,
                    PROTOCOL_VERSION::equals
            );

    public static void registerPackets() {
        int id = 0;
        channel.registerMessage(id++, UpdateAlphaPacket.class, UpdateAlphaPacket::encode, UpdateAlphaPacket::decode, UpdateAlphaPacket::handle);
    }
}