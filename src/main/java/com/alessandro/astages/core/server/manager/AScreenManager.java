package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.holder.ARestrictionHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.core.server.restriction.AScreenRestriction;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.server.AManager;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public class AScreenManager extends AManager<AScreenRestriction, MenuType<?>, AbstractContainerMenu> {
    public AScreenRestriction getRestriction(AHolder holder, AbstractContainerMenu menu, @Nullable BlockState state, @Nullable BlockEntity entity) {
        if (holder.isServerActive()) {
            var serverRestriction = getRestrictions().stream().filter(r ->
                AStagesUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                    r.isRestricted(menu, state, entity)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return getRestrictions().stream().filter(r ->
                AStagesUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                    r.isRestricted(menu, state, entity)
            ).findFirst().orElse(null);
        }

        return null;
    }

    public ARestrictionHolder<AScreenRestriction> getHolder(AHolder holder, AbstractContainerMenu menu, @Nullable BlockState state, @Nullable BlockEntity entity) {
        return ARestrictionHolder.hold(getRestriction(holder, menu, state, entity));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.SCREEN;
    }

//    public final OrderedMultiMap<MenuType<?>, AScreenRestriction> CACHE = OrderedMultiMap.create();

//    @Override
//    public void reloadBeforeScripts() {
//        super.reloadBeforeScripts();
//        CACHE.clear();
//    }

//    @Override
//    public void addRestriction(AScreenRestriction restriction) {
//        super.addRestriction(restriction);
//
//        for (var menu : restriction.getMenus()) {
//            CACHE.put(menu, restriction);
//        }
//    }

//    @Override
//    public AScreenRestriction getRestriction(Player player, MenuType<?> type) {
//        return getRestrictionFromCache(CACHE, type, player);
//    }

//    public AScreenRestriction getRestriction(Player player, MenuType<?> type, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable AbstractContainerMenu container) {
//        var restrictions = CACHE.get(type);
//
//        if (!restrictions.isEmpty()) {
//            for (var restriction : restrictions) {
//                if (!AStagesUtil.hasStage(player, restriction.getStage()) && restriction.isRestricted(type, state, entity, container)) {
//                    return restriction;
//                }
//            }
//        }
//
//        return null;
//    }

//    @Override
//    public void removeRestriction(String id) {
//        super.removeRestriction(id);
//        CACHE.removeValues(restriction -> restriction.getId().equals(id));
//    }
}
