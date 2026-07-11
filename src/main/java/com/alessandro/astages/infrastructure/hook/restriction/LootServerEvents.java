package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.loot.ALootPayload;
import com.alessandro.astages.api.util.APlayerUtils;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

@EventBusSubscriber(modid = AStages.MODID)
public class LootServerEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDrops(LivingDropsEvent event) {
        if (!AStagesCommon.ENABLE_LIVING_DROPS_CHECK.get()) { return; }

        var damageSource = event.getSource();
        var entity = event.getEntity();
        var payload = ALootPayload.create();

        if (damageSource.getDirectEntity() instanceof Player player) {
            payload.player(player);
        } else if (damageSource.getEntity() instanceof Player player) {
            payload.player(player);
        }

        payload
            .entityType(entity.getType())
            .damageType(damageSource.type())
            .position(entity.position());

        var holder = AHolder.serverAndPlayer(payload.player() != null ? payload.player() : APlayerUtils.getNearestPlayer(entity.level(), payload.position()));

        var iterator = event.getDrops().iterator();
        while (iterator.hasNext()) {
            var itemEntity = iterator.next();
            var stack = itemEntity.getItem();
            var restriction = ARestrictionManager.LOOT_INSTANCE.getRestriction(holder, stack, payload);

            if (restriction != null) {
                if (restriction.isEnabled(Attributes.HAS_REPLACER)) {
                    var result = restriction.getReplacer().apply(stack);

                    if (ItemStack.matches(stack, result)) {
                        iterator.remove();
                    } else {
                        itemEntity.setItem(result);
                    }
                } else {
                    iterator.remove();
                }
            }
        }
    }
}
