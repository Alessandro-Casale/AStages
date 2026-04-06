package com.alessandro.astages.infrastructure.mixin.mob;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.event.world.MobInteractEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class AMob {
    @Unique
    private Mob mob$self() {
        return (Mob) (Object) this;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    public void astages$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        var event = new MobInteractEvent(player, hand, mob$self().getType());
        ALoader.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}