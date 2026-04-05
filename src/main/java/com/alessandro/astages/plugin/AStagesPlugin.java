package com.alessandro.astages.plugin;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.plugin.container.AttributeContainer;
import com.alessandro.astages.plugin.container.FolderContainer;
import com.alessandro.astages.plugin.container.ManagerContainer;
import com.alessandro.astages.plugin.container.SimpleRestrictionsContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
@NotNullParamsAndMethodsReturn
public interface AStagesPlugin {
    default void reloadBeforeScripts() { }
    default void clientSynchronization(@Nullable ServerPlayer player) { }
    default void reloadAfterScripts() { }
    default void reloadAfterScripts(MinecraftServer server) { }
    default void clearClientOnLogin() { }
    default void registerManagers(ManagerContainer container) { }
    default void attachAttributes(AttributeContainer container) { }
    default void attachClientAttributes(AttributeContainer container) { }
    default void attachStageAttributes(AttributeContainer container) { }
    default void attachClientStageAttributes(AttributeContainer container) { }
    default void registerConfigFolders(FolderContainer container) { }
    default void registerServerFolders(FolderContainer container) { }
    default void registerSimpleRestriction(SimpleRestrictionsContainer container) { }

    ResourceLocation id();
}
