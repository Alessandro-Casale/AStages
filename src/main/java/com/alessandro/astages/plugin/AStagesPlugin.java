package com.alessandro.astages.plugin;

import com.alessandro.astages.plugin.container.AttributeContainer;
import com.alessandro.astages.plugin.container.FolderContainer;
import com.alessandro.astages.plugin.container.ManagerContainer;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

@NotNullParamsAndMethodsReturn
public interface AStagesPlugin {
    default void reloadBeforeScripts() { }
    default void clientSynchronization(@Nullable ServerPlayer player) { }
    default void reloadAfterScripts() { }
    default void reloadAfterScripts(MinecraftServer server) { }
    default void clearClientOnLogin() { }
    default void registerManagers(ManagerContainer container) { }
    default void attachAttributes(AttributeContainer container) { }
    default void registerFolders(FolderContainer container) { }

    ResourceLocation id();
}
