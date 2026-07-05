package com.alessandro.astages.engine.loot;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.loot.ALootPayload;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.APlayerUtils;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.loot.ALootParams;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

@NotNullParams
public class ALootProcessor {
    public static ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var paramSet = context.getParamOrNull(ALootParams.PARAM_SET_ID);
        if (paramSet == null) { return generatedLoot; }
        var paramSetId = paramSet.toString(); // namespace:path

        var payload = ALootPayload.create();

        switch (paramSetId) {
            case "minecraft:chest" -> applyForChest(payload, context);
            case "minecraft:fishing" -> applyForFishing(payload, context);
            case "minecraft:entity" -> applyForEntity(payload, context);
            case "minecraft:archaeology" -> applyForArchaeology(payload, context);
            case "minecraft:vault" -> applyForVault(payload, context);
            case "minecraft:block" -> applyForBlock(payload, context);
            case "minecraft:command", "minecraft:selector", "minecraft:equipment", "minecraft:gift", "minecraft:barter", "minecraft:advancement_reward", "minecraft:advancement_entity", "minecraft:advancement_location", "minecraft:block_use", "minecraft:generic", "minecraft:shearing", "enchanted_damage", "enchanted_item", "enchanted_location", "enchanted_entity", "hit_block" -> {
                return generatedLoot;
            }
        }

        payload
            .lootTable(context.getQueriedLootTableId())
            .position(context.getParamOrNull(LootContextParams.ORIGIN));

        var holder = AHolder.serverAndPlayer(payload.player() != null ? payload.player() : APlayerUtils.getNearestPlayer(context.getLevel(), payload.position()));

        var iterator = generatedLoot.listIterator();
        while (iterator.hasNext()) {
            var stack = iterator.next();
            var restriction = ARestrictionManager.LOOT_INSTANCE.getRestriction(holder, stack, payload);

            if (restriction != null) {
                if (restriction.isEnabled(Attributes.HAS_REPLACER)) {
                    var result = restriction.getReplacer().apply(stack);

                    if (ItemStack.matches(stack, result)) {
                        iterator.remove();
                    } else {
                        iterator.set(result);
                    }
                } else {
                    iterator.remove();
                }
            }
        }

        return generatedLoot;
    }

    public static void applyForChest(ALootPayload payload, LootContext context) {
        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player) {
            payload.player(player);
        } else if (context.getParamOrNull(LootContextParams.ATTACKING_ENTITY) instanceof Player player) {
            payload.player(player);
        }
    }

    public static void applyForFishing(ALootPayload payload, LootContext context) {
        if (context.getParamOrNull(LootContextParams.ATTACKING_ENTITY) instanceof Player player) {
            payload.player(player);
        } else if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player) {
            payload.player(player);
        }
    }

    public static void applyForEntity(ALootPayload payload, LootContext context) {
        var entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity != null) { payload.entityType(entity.getType()); }

        var damageSource = context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
        if (damageSource != null) { payload.damageType(damageSource.type()); }

        var lastDamagePlayer = context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
        if (lastDamagePlayer != null) {
            payload.player(lastDamagePlayer);
        } else if (context.getParamOrNull(LootContextParams.ATTACKING_ENTITY) instanceof Player player) {
            payload.player(player);
        } else if (context.getParamOrNull(LootContextParams.DIRECT_ATTACKING_ENTITY) instanceof Player player) {
            payload.player(player);
        }
    }

    public static void applyForArchaeology(ALootPayload payload, LootContext context) {
        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player) {
            payload.player(player);
        }
    }

    public static void applyForVault(ALootPayload payload, LootContext context) {
        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player) {
            payload.player(player);
        }
    }

    public static void applyForBlock(ALootPayload payload, LootContext context) {
        payload.blockState(context.getParamOrNull(LootContextParams.BLOCK_STATE));

        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player) {
            payload.player(player);
        }
    }
}