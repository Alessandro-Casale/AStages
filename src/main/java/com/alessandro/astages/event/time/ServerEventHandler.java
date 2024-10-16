package com.alessandro.astages.event.time;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.ATimeRestriction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {

    @SubscribeEvent
    public static void playerTick(TickEvent.@NotNull PlayerTickEvent event) {
//        if (ARestrictionManager.ITEM_INSTANCE.)
//        if (!AStagesUtil.isRealPlayer(event.player)) { return; }
        if (event.player.level().isClientSide) { return; }
        if (event.phase == TickEvent.Phase.END) { return; }

        Player player = event.player;
        if (player.isAlive()) {
            if (player instanceof ServerPlayer serverPlayer) {
                for (String stage : ARestrictionManager.TIME_INSTANCE.getRestrictions().keySet()) {
                    for (ATimeRestriction restriction : ARestrictionManager.TIME_INSTANCE.getRestrictions().get(stage)) {
                        int associatedTimer = getEntityData(serverPlayer, restriction.id);


                    }
                }
            }
        }
    }

    public static void setEntityData(Player player, String tag, int timer) {
        player.getPersistentData().putInt(tag, timer);
    }

    public static int getEntityData(Player player, String tag) {
        return player.getPersistentData().getInt(tag);
    }
}
