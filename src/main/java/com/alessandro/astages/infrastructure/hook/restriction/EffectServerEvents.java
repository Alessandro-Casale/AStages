package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class EffectServerEvents {
    @UnderDevelopment("Check nullability!")
    @SubscribeEvent
    public static void effectAdded(MobEffectEvent.Applicable event) {
        var effectInstance = event.getEffectInstance();
        if (event.getEntity() instanceof ServerPlayer player && effectInstance != null) {
            var effect = effectInstance.getEffect();
            var restriction = ARestrictionManager.EFFECT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), effect.value());

            if (restriction != null) {
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
        }
    }
}
