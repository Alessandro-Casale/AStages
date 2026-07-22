package com.alessandro.astages.api.plugin;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.plugin.container.AttributeContainer;
import com.alessandro.astages.api.plugin.container.FolderContainer;
import com.alessandro.astages.api.plugin.container.SimpleRestrictionsContainer;
import com.alessandro.astages.api.reload.ClientReloadContext;
import com.alessandro.astages.api.reload.ClientReloadPhase;
import com.alessandro.astages.api.reload.McReloadPhase;
import com.alessandro.astages.api.reload.ReloadContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
@NotNullParamsAndMethodsReturn
public interface AStagesPlugin {
    default void onReload(McReloadPhase phase, ReloadContext context) { }
    default void onClientReload(ClientReloadPhase phase, ClientReloadContext context) { }
    default void clientSynchronization(@Nullable ServerPlayer player) { }
    default void clearClientOnLogin(ServerPlayer player) { }
    default void attachAttributes(AttributeContainer container) { }
    default void attachClientAttributes(AttributeContainer container) { }
    default void attachStageAttributes(AttributeContainer container) { }
    default void attachClientStageAttributes(AttributeContainer container) { }
    default void registerConfigFolders(FolderContainer container) { }
    default void registerServerFolders(FolderContainer container) { }
    default void registerSimpleRestriction(SimpleRestrictionsContainer container) { }

    ResourceLocation id();

    static void getDescriptionForReload(McReloadPhase phase, ReloadContext context) {
        AStages.LOGGER.info("[AStagesPlugin] Called method `onReload` for phase {}", phase.name());
    }

    static void getDescriptionForClientReload(ClientReloadPhase phase, ClientReloadContext context) {
        AStages.LOGGER.info("[AStagesPlugin] Called method `onClientReload` for phase {}", phase.name());
    }
}
