package com.alessandro.astages.infrastructure.networking;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.feature.AResettable;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.restriction.AClientRestriction;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@NotNullParamsAndMethodsReturn
public interface ASyncRestrictionPacket<R extends AClientRestriction<?, ?, ?> & AResettable> extends ARestrictionPacket {
    AClientMinimalManager<?, ?> manager();
    Class<R> restrictionClass();
    R instantiateRestriction();
    void fillContents(R restriction);
    void register(R restriction);

    @Override
    default void run(IPayloadContext context) {
        var existing = manager().getRestriction(id());

        if (restrictionClass().isInstance(existing) && existing.getStage().equals(stage())) {
            var restriction = restrictionClass().cast(existing);
            restriction.clearContents();
            fill(restriction);

            return;
        }

        if (existing != null) {
            // Should never happen: the server never changes the type or stage of a registered restriction,
            // and removals are synced before a new restriction with the same id can arrive.
            AStages.LOGGER.error("Restriction {} arrived with a different type or stage than the client's one: this is a bug, replacing it", id());
            manager().removeRestriction(id());
        }

        var restriction = instantiateRestriction();
        fill(restriction);
        register(restriction);
    }

    private void fill(R restriction) {
        restriction.setPriority(priority());

        var synced = attributes();
        if (synced != null) {
            for (var attribute : synced.keySet()) {
                restriction.setFromData(data(), attribute);
            }
        }

        fillContents(restriction);
    }
}
