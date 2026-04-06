package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.restriction.ADimensionRestriction;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.text.DecimalFormat;
import java.util.Objects;

@UnderDevelopment("Replace persistentData system!")
@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class DimensionServerEvents {
    @SubscribeEvent
    public static void onEntityTravel(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof Player player) {
            ResourceLocation dimension = event.getDimension().location();

            ResourceLocation currentDimension = player.level().dimension().location();
            ADimensionRestriction fromDim = ARestrictionManager.DIMENSION_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), currentDimension);
            ADimensionRestriction toDim = ARestrictionManager.DIMENSION_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), dimension);

            if (fromDim != null && fromDim.isEnabled(Attributes.BIDIRECTIONAL)) {
                event.setCanceled(true);
                fromDim.displayMessage(Attributes.Dimension.LEAVE_MESSAGE, dimension, player);
            } else if (toDim != null && toDim.getMaxStayTimer() == null) {
                event.setCanceled(true);
                toDim.displayMessage(Attributes.Dimension.ENTER_MESSAGE, dimension, player);
            }

            if (toDim != null && toDim.getMaxStayTimer() != null) {
                var persistentData = player.getPersistentData();
                if (persistentData.contains(toDim.getNbtAccess())) {
                    var accesses = persistentData.getInt(toDim.getNbtAccess());
                    if (accesses >= toDim.get(Attributes.MAX_ACCESS)) {
                        event.setCanceled(true);
                        toDim.displayMessage(Attributes.Dimension.EXPIRED_MESSAGE, dimension, player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            var persistentData = player.getPersistentData();
            ResourceLocation currentDimension = player.level().dimension().location();

            var restriction = ARestrictionManager.DIMENSION_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), currentDimension);

            if (restriction != null && restriction.getMaxStayTimer() != null) {
                var nbtId = restriction.getNbtId();

                if (!persistentData.contains(nbtId)) {
                    persistentData.putInt(nbtId, restriction.getMaxStayTimer());
                } else {
                    var currentTicks = persistentData.getInt(nbtId);

                    if (currentTicks < 0) {
                        persistentData.putInt(nbtId, restriction.getMaxStayTimer());
                        currentTicks = restriction.getMaxStayTimer();
                    }

                    currentTicks = currentTicks - 1;
                    persistentData.putInt(nbtId, currentTicks);

                    showTimer(player, currentTicks, restriction.get(Attributes.CHAT_FORMATTING));

                    if (currentTicks == 0) {
                        persistentData.remove(nbtId);
                        addAccess(persistentData, restriction);
                        player.displayClientMessage(Component.empty(), true); // Reset action bar before teleporting!

                        var server = player.server;
                        var dim = Objects.requireNonNull(server.getLevel(player.getRespawnDimension()));
                        var pos = player.getRespawnPosition();
                        var overworld = player.server.overworld();

                        if (pos != null) {
                            var respawnPosition = player.findRespawnPositionAndUseSpawnBlock(true, DimensionTransition.DO_NOTHING);

                            player.teleportTo(respawnPosition.newLevel(), respawnPosition.pos().x, respawnPosition.pos().y, respawnPosition.pos().z, respawnPosition.xRot(), respawnPosition.yRot());
                        } else {
                            player.teleportTo(overworld, overworld.getSharedSpawnPos().getX(), overworld.getSharedSpawnPos().getY(), overworld.getSharedSpawnPos().getZ(), overworld.getSharedSpawnAngle(), overworld.getSharedSpawnAngle());
                        }

                        var currentAccess = persistentData.getInt(restriction.getNbtAccess());
                        var leftAccess = restriction.get(Attributes.MAX_ACCESS) - currentAccess;
                        if (leftAccess > 0) {
                            player.sendSystemMessage(Component.translatable("message.astages.dimension.access.left", leftAccess).withStyle(ChatFormatting.GREEN), true);
                        } else {
                            player.sendSystemMessage(Component.translatable("message.astages.dimension.access.zero").withStyle(ChatFormatting.RED), true);
                        }
                    }
                }
            }
        }
    }

    private static void addAccess(CompoundTag persistentData, ADimensionRestriction restriction) {
        if (!persistentData.contains(restriction.getNbtAccess())) {
            persistentData.putInt(restriction.getNbtAccess(), 1);
        } else {
            persistentData.putInt(restriction.getNbtAccess(), persistentData.getInt(restriction.getNbtAccess()) + 1);
        }
    }

    private static void showTimer(ServerPlayer player, int ticks, ChatFormatting formatting) {
        DecimalFormat formatter = new DecimalFormat("00");

        int tickForCalculations = ticks;
        int hours = (int) Math.floor(tickForCalculations / 72000d);
        tickForCalculations = tickForCalculations % 72000;
        int minutes = (int) Math.floor(tickForCalculations / 1200d);
        tickForCalculations = tickForCalculations % 1200;
        int seconds = (int) Math.floor(tickForCalculations / 20d);

        Component message = Component.literal(formatter.format(hours) + ":" + formatter.format(minutes) + ":" + formatter.format(seconds)).withStyle(formatting);
        player.displayClientMessage(message, true);
    }
}
