package com.github.tacowasa059.transparentplayermod;

import com.github.tacowasa059.transparentplayermod.capabilities.entity.AlphaValue;
import com.github.tacowasa059.transparentplayermod.capabilities.entity.StorageAlphaValue;
import com.github.tacowasa059.transparentplayermod.packet.PacketHandler;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TransparentPlayerMod.MOD_ID)
public class TransparentPlayerMod {
    public static final String MOD_ID="transparentplayermod";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public TransparentPlayerMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        CapabilityManager.INSTANCE.register(AlphaValue.class, new StorageAlphaValue(), AlphaValue::new);
        PacketHandler.registerPackets();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().debug);
    }
}
