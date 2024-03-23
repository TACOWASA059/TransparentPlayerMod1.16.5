package com.github.tacowasa059.transparentplayermod.events.commands;

import com.github.tacowasa059.transparentplayermod.TransparentPlayerMod;
import com.github.tacowasa059.transparentplayermod.capabilities.provider.AlphaValueProvider;
import com.github.tacowasa059.transparentplayermod.utils.ModUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ObjectiveArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = TransparentPlayerMod.MOD_ID)
public class CommandRegister {

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("transparent")
                .requires(source -> source.hasPermissionLevel(2)) // コマンドの実行に必要な権限レベル
                .then(Commands.literal("data")
                    .then(Commands.literal("get")
                            .then(Commands.argument("players", EntityArgument.players())
                                .executes(context -> {
                                        CommandSource source = context.getSource();
                                        Collection<ServerPlayerEntity> playerEntitylist=EntityArgument.getPlayers(context,"players");
                                        for(ServerPlayerEntity playerEntity: playerEntitylist) {
                                            playerEntity.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue -> {
                                                int current = alphaValue.getAlpha();
                                                source.sendFeedback(new StringTextComponent(TextFormatting.GREEN+playerEntity.getName().getString()+":アルファ:"+ TextFormatting.AQUA+current), false);
                                            });
                                        }
                                    return 1;

                                })))
                        .then(Commands.literal("set")
                                .then(Commands.argument("players", EntityArgument.players())
                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                    .executes(context -> {
                                        int value = IntegerArgumentType.getInteger(context, "value");
                                        setValue(context, value,true);
                                        return 1;
                                    }
                                    )
                                )
                                )
                        )
                        .then(Commands.literal("reset")
                                .then(Commands.argument("players", EntityArgument.players())
                                    .executes(context -> {
                                        int value = 255;
                                        setValue(context, value,true);
                                        return 1;
                                    }
                                    )
                                )
                        )
                        .then(Commands.literal("add")
                            .then(Commands.argument("players", EntityArgument.players())
                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                    .executes(context -> {
                                        int value = IntegerArgumentType.getInteger(context, "value");;
                                        setValue(context, value,false);
                                        return 1;
                                    }
                                    )
                                )
                            )
                        )
                    .then(Commands.literal("subtract")
                        .then(Commands.argument("players", EntityArgument.players())
                            .then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes(context -> {
                                    int value = -IntegerArgumentType.getInteger(context, "value");
                                    setValue(context, value,false);
                                    return 1;
                                    }
                                )
                            )
                        )
                    )
                )
            .then(Commands.literal("scoreboard")
                .then(Commands.argument("players",EntityArgument.players())
                    .then(Commands.literal("set_score")
                        .then(Commands.argument("objectives", ObjectiveArgument.objective())
                            .executes(context -> {
                                    Scoreboard scoreboard=context.getSource().getServer().getScoreboard();
                                    CommandSource source = context.getSource();
                                    Collection<ServerPlayerEntity> playerEntitylist= null;
                                    try {
                                        playerEntitylist = EntityArgument.getPlayers(context,"players");
                                    } catch (CommandSyntaxException e) {
                                        source.sendFeedback(new StringTextComponent(TextFormatting.RED+"コマンドの引数が間違っています。"),false);
                                    }
                                    ScoreObjective objective=ObjectiveArgument.getObjective(context,"objectives");
                                    for(ServerPlayerEntity playerEntity: Objects.requireNonNull(playerEntitylist)) {
                                        playerEntity.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue -> {
                                            int current = alphaValue.getAlpha();
                                            Score score = scoreboard.getOrCreateScore(playerEntity.getName().getString(), objective);
                                            score.setScorePoints(current);
                                            source.sendFeedback(new StringTextComponent(TextFormatting.GREEN+"スコアボードに反映しました"), false);
                                        });
                                    }

                                    return 1;
                                }
                            )
                        )
                    )
                    .then(Commands.literal("set_from")
                            .then(Commands.argument("objectives", ObjectiveArgument.objective())
                                    .executes(context -> {
                                            Scoreboard scoreboard=context.getSource().getServer().getScoreboard();
                                            CommandSource source = context.getSource();
                                            Collection<ServerPlayerEntity> playerEntitylist=EntityArgument.getPlayers(context,"players");
                                            ScoreObjective objective=ObjectiveArgument.getObjective(context,"objectives");
                                            for(ServerPlayerEntity playerEntity: Objects.requireNonNull(playerEntitylist)) {
                                                playerEntity.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue -> {
                                                    Score score = scoreboard.getOrCreateScore(playerEntity.getName().getString(), objective);
                                                    int current=score.getScorePoints();
                                                    alphaValue.setAlpha(current);
                                                    ModUtils.sendOnetoAllClient( playerEntity.getUniqueID(), current);
                                                    source.sendFeedback(new StringTextComponent(TextFormatting.GREEN+"スコアボードの値を反映しました"), false);
                                                });
                                            }

                                            return 1;
                                        }
                                    )
                            )
                    )

                )
            )
        );
    }

    private static void setValue(CommandContext<CommandSource> context, int value,boolean flag) {
        CommandSource source = context.getSource();
        try {
            SetCommand(context, source, value, flag);
        } catch (CommandSyntaxException e) {
            source.sendFeedback(new StringTextComponent(TextFormatting.RED+"コマンドの引数が間違っています。"),false);
        }
    }

    private static void SetCommand(CommandContext<CommandSource> context, CommandSource source, int value, boolean flag) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> playerEntitylist=EntityArgument.getPlayers(context,"players");
        for(ServerPlayerEntity playerEntity:playerEntitylist) {
            playerEntity.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue -> {
                int data;
                data=flag?value:value+alphaValue.getAlpha();
                alphaValue.setAlpha(data);

                UUID uuid = playerEntity.getUniqueID();
                ModUtils.sendOnetoAllClient( uuid, data);
            });
        }
        source.sendFeedback(new StringTextComponent(TextFormatting.GREEN + "アルファの値を変更しました。"), false);
    }
}
