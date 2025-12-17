package com.alessandro.astages.event.region;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.APlayerUtils;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.PositionCheck event) {
        var pos = new BlockPos((int) event.getX(), (int) event.getY(), (int) event.getZ());
        var vec = new Vec3((int) event.getX(), (int) event.getY(), (int) event.getZ());

        var player = APlayerUtils.getNearestPlayer(event.getLevel().getLevel(), vec);

        if (canBeRunForPlayer(player)) {
            var restriction = ARestrictionManager.REGION_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), pos);

            if (restriction != null && restriction.isDisabled(Attributes.MOB_SPAWNING)) {
                var restrictedDimension = restriction.get(Attributes.DIMENSION);

                if (restrictedDimension == null || event.getLevel().getLevel().dimension().location().equals(restrictedDimension)) {
                    event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onGenericInteraction(PlayerInteractEvent.LeftClickBlock event) {
        commonInteractOperations(event);
    }

    @SubscribeEvent
    public static void onGenericInteraction(PlayerInteractEvent.RightClickBlock event) {
        commonInteractOperations(event);
    }

    @SubscribeEvent
    public static void onGenericInteraction(PlayerInteractEvent.RightClickItem event) {
        commonInteractOperations(event);
    }

    @SubscribeEvent
    public static void onGenericInteraction(PlayerInteractEvent.EntityInteract event) {
        commonInteractOperations(event);
    }

    @SubscribeEvent
    public static void onGenericInteraction(PlayerInteractEvent.EntityInteractSpecific event) {
        commonInteractOperations(event);
    }

    private static <T extends PlayerInteractEvent & ICancellableEvent> void commonInteractOperations(T event) {
        if (!event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.REGION_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), event.getPos());

            if (restriction != null && restriction.isDisabled(Attributes.GENERIC_INTERACTIONS)) {
                var restrictedDimension = restriction.get(Attributes.DIMENSION);

                if (restrictedDimension == null || event.getLevel().dimension().location().equals(restrictedDimension)) {
                    event.setCanceled(true);
                    serverPlayer.displayClientMessage(restriction.get(Attributes.Region.INTERACT_MESSAGE).get(), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonation(ExplosionEvent.Detonate event) {
        var player = APlayerUtils.getNearestPlayer(event.getLevel(), event.getExplosion().center());

        if (canBeRunForPlayer(player)) {
            var rawPos = event.getExplosion().center();
            var pos = new BlockPos((int) rawPos.x, (int) rawPos.y, (int) rawPos.z);
            var restriction = ARestrictionManager.REGION_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), pos);

            if (restriction != null) {
                var restrictedDimension = restriction.get(Attributes.DIMENSION);

                if (restrictedDimension == null || event.getLevel().dimension().location().equals(restrictedDimension)) {
                    if (restriction.isDisabled(Attributes.EXPLOSIONS_AFFECT_BLOCKS)) {
                        event.getAffectedBlocks().clear();
                    }

                    if (restriction.isDisabled(Attributes.EXPLOSIONS_AFFECT_ENTITIES)) {
                        event.getAffectedEntities().clear();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCommandExecuted(CommandEvent event) {
        var serverPlayer = event.getParseResults().getContext().getSource().getPlayer();
        var stringCommand = event.getParseResults().getReader().getString();

        if (canBeRunForPlayer(serverPlayer)) {
            var restriction = ARestrictionManager.REGION_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), serverPlayer.getOnPos());

            if (restriction != null && restriction.isDisabled(Attributes.PERFORM_COMMANDS)) {
                var foundedCommands = restriction.getDisabledCommands().stream().filter(stringCommand::contains).count();

                if (foundedCommands != 0) {
                    event.setCanceled(true);
                    restriction.displayMessage(Attributes.Region.COMMAND_MESSAGE, stringCommand, serverPlayer);
                }
            }
        }
    }

    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
