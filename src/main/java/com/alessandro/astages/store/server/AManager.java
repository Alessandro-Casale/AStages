package com.alessandro.astages.store.server;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.holder.ARestrictionHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.ARestrictionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for almost all Managers related to AStages!
 *
 * @param <R> The restriction associated to this manager
 * @param <U> For restrict method object type
 * @param <V> For isRestricted method object type
 */
@NotNullParams
public abstract class AManager<R extends ARestriction<R, U, V>, U, V> implements AMinimalManager<R>/*, ServerStageReadable<R, V>*/ {
    private final List<R> restrictions = new ArrayList<>();
    private final Map<String, R> IDS = new HashMap<>();

    public List<R> getRestrictions() {
        return restrictions;
    }

    @Override
    public void reloadBeforeScripts() {
        restrictions.clear();
        IDS.clear();
    }

    @Override
    public void reloadAfterScripts() { }

    public List<String> getIds() {
        return IDS.keySet().stream().toList();
    }

    public void addRestriction(R restriction) {
        if (IDS.containsKey(restriction.getId())) {
            if (AStagesCommon.ENABLE_LOGS.get()) {
                AStages.LOGGER.warn("Restriction with id {} already found!", restriction.getId());
            }

            return;
        }

        IDS.put(restriction.getId(), restriction);
        restrictions.add(restriction);
        ARestrictionManager.ALL_IDS.add(restriction.getId());
        if (considerGlobalStages()) { ARestrictionManager.ALL_STAGES.add(restriction.getStage()); }
    }

    @Override
    public R getRestriction(String id) {
        return IDS.getOrDefault(id, null);
    }

    public ARestrictionHolder<R> getHolder(String id) {
        return ARestrictionHolder.hold(getRestriction(id));
    }

    public R getRestriction(AHolder holder, V object) {
        if (holder.isServerActive()) {
            var serverRestriction = restrictions.stream().filter(r ->
                !AStagesUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                r.isRestricted(object)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return restrictions.stream().filter(r ->
                !AStagesUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                r.isRestricted(object)
            ).findFirst().orElse(null);
        }

        return null;
    }

    public ARestrictionHolder<R> getHolder(AHolder holder, V object) {
        return ARestrictionHolder.hold(getRestriction(holder, object));
    }

    @Override
    public void removeRestriction(String id) {
        IDS.remove(id);
        restrictions.removeIf(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return null;
    }

    public boolean considerGlobalStages() {
        return true;
    }
}
