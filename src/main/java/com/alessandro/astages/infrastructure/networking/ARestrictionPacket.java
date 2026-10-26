package com.alessandro.astages.infrastructure.networking;

import com.alessandro.astages.api.network.data.RestrictionData;
import com.alessandro.astages.api.store.container.SyncableAttributeStore;

public interface ARestrictionPacket extends AStagesPacket {
    RestrictionData data();

    default String id() {
        return data().id();
    }

    default String stage() {
        return data().stage();
    }

    default int priority() {
        return data().priority();
    }

    default SyncableAttributeStore attributes() {
        return data().attributes();
    }

//    default <T> T getAttributeFromData(Attribute<T> attribute) {
//        return attributes().getAttribute(attribute);
//    }
}
