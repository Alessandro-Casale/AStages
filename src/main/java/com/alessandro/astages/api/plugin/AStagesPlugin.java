package com.alessandro.astages.api.plugin;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.plugin.container.AttributeContainer;
import com.alessandro.astages.api.plugin.container.FolderContainer;
import com.alessandro.astages.api.plugin.container.SimpleRestrictionsContainer;
import com.alessandro.astages.api.reload.AStagesReloadPhase;
import com.alessandro.astages.api.reload.ReloadContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
@NotNullParamsAndMethodsReturn
public interface AStagesPlugin {
    default void onReload(AStagesReloadPhase phase, ReloadContext context) { }
    default void clientSynchronization(@Nullable ServerPlayer player) { }
    default void clearClientOnLogin() { }
    default void attachAttributes(AttributeContainer container) { }
    default void attachClientAttributes(AttributeContainer container) { }
    default void attachStageAttributes(AttributeContainer container) { }
    default void attachClientStageAttributes(AttributeContainer container) { }
    default void registerConfigFolders(FolderContainer container) { }
    default void registerServerFolders(FolderContainer container) { }
    default void registerSimpleRestriction(SimpleRestrictionsContainer container) { }

    ResourceLocation id();
}
