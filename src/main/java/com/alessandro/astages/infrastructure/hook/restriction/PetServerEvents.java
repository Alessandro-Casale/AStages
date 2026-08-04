package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.AnimalTameEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class PetServerEvents {
    @SubscribeEvent
    public static void onPlayerTame(AnimalTameEvent event) {
        if (!event.getTamer().level().isClientSide) {
            var player = event.getTamer();
            var pet = event.getEntity();

            var restriction = ARestrictionManager.PET_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), pet.getType());

            if (restriction != null && restriction.isDisabled(Attributes.TAMING)) {
                event.setCanceled(true);

                restriction.displayMessage(Attributes.Pet.TAME_MESSAGE, pet, player);
            }
        }
    }

    @SubscribeEvent
    public static void onMount(EntityMountEvent event) {
        if (!event.getEntityMounting().level().isClientSide) {
            var entity = event.getEntityMounting();
            var pet = event.getEntityBeingMounted();

            if (entity instanceof Player player) {
                var restriction = ARestrictionManager.PET_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), pet.getType());

                if (restriction != null && restriction.isDisabled(Attributes.MOUNTING)) {
                    event.setCanceled(true);

                    restriction.displayMessage(Attributes.Pet.MOUNT_MESSAGE, pet, player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerBreedEntity(PlayerInteractEvent.EntityInteract event) {
        if (!event.getEntity().level().isClientSide) {
            var player = event.getEntity();
            var pet = event.getTarget();
            var item = event.getEntity().getItemInHand(event.getHand());

            var restriction = ARestrictionManager.PET_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), pet.getType());

            if (restriction != null && restriction.isDisabled(Attributes.BREEDING) && !item.isEmpty()) {
                event.setCanceled(true);

                restriction.displayMessage(Attributes.Pet.BREED_MESSAGE, pet, player);
            }
        }
    }
}
