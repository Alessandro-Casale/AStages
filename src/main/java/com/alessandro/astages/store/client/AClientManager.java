package com.alessandro.astages.store.client;

import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.store.ARestrictionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NotNullParams
public abstract class AClientManager<R extends AClientRestriction<R, U, V>, U, V> implements AClientMinimalManager<R> {
    private final List<R> restrictions = new ArrayList<>();
    private final Map<String, R> IDS = new HashMap<>();

    public List<R> getRestrictions() {
        return restrictions;
    }

    public void reloadBeforeScripts() {
        restrictions.clear();
        IDS.clear();
    }

    public void reloadAfterScripts() { }

    public void addRestriction(R restriction) {
        IDS.put(restriction.getId(), restriction);
        restrictions.add(restriction);
    }

    @Override
    public R getRestriction(String id) {
        return IDS.getOrDefault(id, null);
    }

    public R getRestriction(AClientHolder holder, V object) {
        if (holder.isServerActive()) {
            var serverRestriction = restrictions.stream().filter(r ->
                AStagesClientUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                r.isRestricted(object)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return restrictions.stream().filter(r ->
                AStagesClientUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                r.isRestricted(object)
            ).findFirst().orElse(null);
        }

        return null;
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
}
