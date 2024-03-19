package com.github.tacowasa059.transparentplayermod.events.commands;

import com.github.tacowasa059.transparentplayermod.TransparentPlayerMod;
import com.github.tacowasa059.transparentplayermod.capabilities.provider.AlphaValueProvider;
import com.github.tacowasa059.transparentplayermod.packet.PacketHandler;
import com.github.tacowasa059.transparentplayermod.packet.packets.sendCommandUpdatePacket;
import com.github.tacowasa059.transparentplayermod.utils.ModUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(modid = TransparentPlayerMod.MOD_ID)
public class CommandRegister {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        if (FMLEnvironment.dist.isClient()) {
            onClientCommandRegister(event);
        }else{
            onServerCommandRegister(event);
        }
    }
    public static void onServerCommandRegister(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("transparent")
                .requires(source -> source.hasPermissionLevel(2)) // コマンドの実行に必要な権限レベル
                .then(Commands.literal("get")
                        .then(Commands.argument("player", EntityArgument.players())
                            .executes(context -> {
                                CommandSource source = context.getSource();
                                Collection<ServerPlayerEntity> playerEntitylist=EntityArgument.getPlayers(context,"player");
                                for(ServerPlayerEntity playerEntity:playerEntitylist) {
                                    playerEntity.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue -> {
                                        int current = alphaValue.getAlpha();
                                        source.sendFeedback(new StringTextComponent(TextFormatting.GREEN+playerEntity.getName().getString()+":Value:"+ TextFormatting.AQUA+current), false);
                                    });
                                }
                                return 1;
                            })))
                    .then(Commands.literal("set")
                            .then(Commands.argument("player", EntityArgument.players())
                            .then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes(context -> {
                                    CommandSource source = context.getSource();
                                    int value = IntegerArgumentType.getInteger(context, "value");
                                            ServerSetCommand(context, source, value,true);
                                            return 1;
                                }
                                )
                            )
                            )
                    )
                    .then(Commands.literal("reset")
                            .then(Commands.argument("player", EntityArgument.players())
                                            .executes(context -> {
                                                        CommandSource source = context.getSource();
                                                        int value = 255;
                                                ServerSetCommand(context, source, value,true);
                                                return 1;
                                                    }
                                            )
                            )
                    )
                    .then(Commands.literal("add")
                            .then(Commands.argument("player", EntityArgument.players())
                                    .then(Commands.argument("value", IntegerArgumentType.integer())
                                            .executes(context -> {
                                                        CommandSource source = context.getSource();
                                                        int value = IntegerArgumentType.getInteger(context, "value");
                                                        ServerSetCommand(context, source, value,false);
                                                        return 1;
                                                    }
                                            )
                                    )
                            )
                    )
                .then(Commands.literal("subtract")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                        .executes(context -> {
                                                    CommandSource source = context.getSource();
                                                    int value = -IntegerArgumentType.getInteger(context, "value");
                                                    ServerSetCommand(context, source, value,false);
                                                    return 1;
                                                }
                                        )
                                )
                        )
                )
        )
        ;
    }

    private static void ServerSetCommand(CommandContext<CommandSource> context, CommandSource source, int value,boolean flag) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> playerEntitylist=EntityArgument.getPlayers(context,"player");
        for(ServerPlayerEntity playerEntity:playerEntitylist) {
            playerEntity.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue -> {
                int data;
                data=flag?value:value+alphaValue.getAlpha();
                alphaValue.setAlpha(data);

                UUID uuid = playerEntity.getUniqueID();
                MinecraftServer server = source.getServer();
                ModUtils.sendOnetoAllClient(server, uuid, data);
            });
        }
        source.sendFeedback(new StringTextComponent(TextFormatting.GREEN + "modified Alpha Value"), false);
    }

    public static void onClientCommandRegister(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("transparent")
                .requires(source -> source.hasPermissionLevel(2)) // コマンドの実行に必要な権限レベル
                .then(Commands.literal("get")
                        .then(Commands.argument("player", EntityArgument.players())
                                .executes(context -> {
                                    CommandSource source = context.getSource();
                                    Collection<ServerPlayerEntity> playerEntitylist=EntityArgument.getPlayers(context,"player");

                                    for(ServerPlayerEntity playerEntity:playerEntitylist){
                                        playerEntity.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue -> {
                                            int current = alphaValue.getAlpha();
                                            source.sendFeedback(new StringTextComponent(TextFormatting.GREEN+playerEntity.getName().getString()+":Value:"+ TextFormatting.AQUA+current), false);
                                        });
                                    }
                                    return 1;
                                })))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            CommandSource source = context.getSource();
                                            int value = IntegerArgumentType.getInteger(context, "value");
                                            clientSetCommand(context, source, value,true);
                                            return 1;
                                        }
                                )
                        )
                )
                )
                .then(Commands.literal("reset")
                        .then(Commands.argument("player", EntityArgument.players())
                                        .executes(context -> {
                                                    CommandSource source = context.getSource();
                                                    int value = 255;
                                                    clientSetCommand(context, source, value,true);
                                            return 1;
                                                }
                                        )
                        )
                )
                .then(Commands.literal("add")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                    .executes(context -> {
                                                CommandSource source = context.getSource();
                                                int value = IntegerArgumentType.getInteger(context, "value");
                                                clientSetCommand(context, source, value,false);
                                                return 1;
                                            }
                                    )
                                )
                        )
                )
                .then(Commands.literal("subtract")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                    .executes(context -> {
                                                CommandSource source = context.getSource();
                                                int value = -IntegerArgumentType.getInteger(context, "value");
                                                clientSetCommand(context, source, value,false);
                                                return 1;
                                            }
                                    )
                                )
                        )
                )
        )
        ;
    }

    //flag:true 値をそのまま入れる
    //flag:false 元の値に足し合わせる
    private static void clientSetCommand(CommandContext<CommandSource> context, CommandSource source, int value,boolean flag) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> playerEntitylist=EntityArgument.getPlayers(context,"player");

        for(ServerPlayerEntity playerEntity:playerEntitylist) {
            if(flag){
                sendCommandUpdatePacket sendCommandUpdatePacket=(new sendCommandUpdatePacket(playerEntity.getUniqueID(), value));
                PacketHandler.channel.sendToServer(sendCommandUpdatePacket);
            }
            else{
                playerEntity.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue ->{
                    sendCommandUpdatePacket sendCommandUpdatePacket=(new sendCommandUpdatePacket(playerEntity.getUniqueID(), value + alphaValue.getAlpha()));
                    PacketHandler.channel.sendToServer(sendCommandUpdatePacket);
                });
            }
        }
        source.sendFeedback(new StringTextComponent(TextFormatting.GREEN + "Sent to the server"), false);
    }
}
