package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class PlayerEventHandler {
//    public static void onServerLoaded(ServerEvent.Lo)

    @SubscribeEvent
    public static void playerTick(TickEvent.@NotNull PlayerTickEvent event) {
//        if (event.side == LogicalSide.SERVER) {
//
//            event.player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
//                AStages.LOGGER.debug("Server: {}", playerStage.getStages());
//            });
//        }
//
//        if (event.side == LogicalSide.CLIENT) {
//            AStages.LOGGER.debug("Client: {}", ClientPlayerStage.getPlayerStages());
//        }

//        AStages.LOGGER.debug(String.valueOf(ARestrictionManager.ITEM_INSTANCE.restrictions.size()));
//        AStages.LOGGER.debug(ARestrictionManager.ITEM_INSTANCE.restrictions.toString());

//        Predicate<Integer> p1 = (i) -> i == 2;
//        Predicate<Integer> p2 = (i) -> i == 2;
        // AStages.LOGGER.debug(String.valueOf(Predicate.isEqual(p1).equals(p2)));
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.@NotNull PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide) {
            event.getEntity().getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> playerStage.setChangedFor(event.getEntity(), PlayerStage.Operation.GET, null));
        }
    }

    @SubscribeEvent
    public static void onAttachedCapabilities(@NotNull AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerStageProvider.PLAYER_STAGE).isPresent()) {
                event.addCapability(new ResourceLocation(AStages.MODID, "properties"), new PlayerStageProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.@NotNull Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
}