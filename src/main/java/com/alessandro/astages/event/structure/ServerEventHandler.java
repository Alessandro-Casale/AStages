package com.alessandro.astages.event.structure;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARecipeManager;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.AStructureRestriction;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.Info;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    public static final Map<UUID, Boolean> playerIsInStructure = new HashMap<>();

    // Check if player is in a structure
    @Info("To be tested!")
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
        if (ARestrictionManager.STRUCTURE_INSTANCE.getRestrictions().isEmpty()) { return; }
        if (event.player.level().isClientSide) { return; }
        if (event.phase == TickEvent.Phase.START) { return; }
        if (event.player.getServer() == null) { return; }

//        if (event.player instanceof ServerPlayer player) {
//            UUID playerUUID = player.getUUID();
//            String stage = "structure1";
//
//            StructureManager manager = Objects.requireNonNull(player.getServer().getLevel(player.level().dimension())).structureManager();
//
//            Structure structure = manager.registryAccess().registryOrThrow(Registries.STRUCTURE).get(new ResourceLocation("minecraft", "pillager_outpost"));
//            if (structure != null) {
//                boolean isInStructure = manager.getStructureAt(player.getOnPos(), structure).isValid();
//
//                playerIsInStructure.put(playerUUID, isInStructure);
//            }
//        }

        if (event.player instanceof ServerPlayer player) {
            StructureManager manager = Objects.requireNonNull(player.getServer().getLevel(player.level().dimension())).structureManager();
            UUID playerUUID = player.getUUID();

            ARestrictionManager.STRUCTURE_INSTANCE.getRestrictions().forEach((stage, restrictions) -> {
                if (!AStagesUtil.hasStage(player, stage)) {
                    for (var restriction : restrictions) {
                        for (var structureResource : restriction.structures) {
                            Structure structure = manager.registryAccess().registryOrThrow(Registries.STRUCTURE).get(structureResource);

                            if (structure != null) {
                                boolean isInStructure = manager.getStructureAt(player.getOnPos(), structure).isValid();
                                if (isInStructure && !playerIsInStructure.get(playerUUID)) {
                                    playerIsInStructure.put(playerUUID, true);
                                } else if (!isInStructure && playerIsInStructure.get(playerUUID)) {
                                    playerIsInStructure.put(playerUUID, false);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    // public static void onPlayerMoved(PlayerEvent.Mo)

    @Info("Player can't break block in the structure")
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        for (UUID uuid : playerIsInStructure.keySet()) {
            if (event.getPlayer().getUUID().equals(uuid) && playerIsInStructure.get(uuid)) {
                event.setCanceled(true);
                event.setResult(Event.Result.DENY);
                AStages.LOGGER.debug("Can't break this structure!");
            }
        }
    }

    @Info("To be tested!")
    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent event) {
        for (UUID uuid : playerIsInStructure.keySet()) {
            if (event.getEntity().getUUID().equals(uuid) && playerIsInStructure.get(uuid)) {
                event.setCanceled(true);
                event.setResult(Event.Result.DENY);
                AStages.LOGGER.debug("Can't interact with this structure!");
            }
        }
    }
}
