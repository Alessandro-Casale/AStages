package com.alessandro.astages.core;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.ARestrictionStage;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.develop.NotYetImplemented;
import com.alessandro.astages.api.event.AddRestrictionEvent;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.capability.ServerStage;
import com.alessandro.astages.core.server.manager.*;
import com.alessandro.astages.event.CommonEventSettings;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.dimension.DimensionIdsSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.networking.packet.simple.SimpleIdsSyncerS2CPacket;
import com.alessandro.astages.networking.packet.stages.ServerStagesSyncerS2CPacket;
import com.alessandro.astages.networking.packet.stages.StagesSyncerS2CPacket;
import com.alessandro.astages.plugin.APluginManager;
import com.alessandro.astages.plugin.AStagesPlugin;
import com.alessandro.astages.plugin.ForPlugins;
import com.alessandro.astages.simple.ASimpleRestrictionManager;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.server.AMinimalManager;
import com.alessandro.astages.util.ReloadType;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.*;

@NotNullParams
public class ARestrictionManager {
    private static final Map<ARestrictionType, AMinimalManager<?>> ASSOCIATION_MAP = new HashMap<>();
    @ForPlugins public static final Map<ARestrictionType, AMinimalManager<?>> EXTERNAL_MANAGERS = new HashMap<>();
    @ForPlugins public static final Map<Class<?>, AttributeStore> ATTACHED_ATTRIBUTES = new HashMap<>();

    // ADD SLOT RESTRICTION
    public static final AItemManager ITEM_INSTANCE = new AItemManager(); // Done!
    public static final ADimensionManager DIMENSION_INSTANCE = new ADimensionManager(); // Done!
    public static final AMobManager MOB_INSTANCE = new AMobManager(); // Done!
    public static final AStructureManager STRUCTURE_INSTANCE = new AStructureManager(); // Done!
    public static final ARecipeManager RECIPE_INSTANCE = new ARecipeManager(); // Done!
    public static final AScreenManager SCREEN_INSTANCE = new AScreenManager(); // Done!
    public static final AOreManager ORE_INSTANCE = new AOreManager(); // Implement Server Stages
    public static final APetManager PET_INSTANCE = new APetManager(); // Done!
    public static final AEnchantManager ENCHANT_INSTANCE = new AEnchantManager(); // Done!
    public static final ACropManager CROP_INSTANCE = new ACropManager();  // Done!
    public static final AEffectManager EFFECT_INSTANCE = new AEffectManager(); // Done!
    public static final ARegionManager REGION_INSTANCE = new ARegionManager(); // Done!
    public static final ALootManager LOOT_INSTANCE = new ALootManager(); // Done!

    public static Set<String> ALL_STAGES = new HashSet<>();
    public static Set<String> ALL_IDS = new HashSet<>();
    public static Set<String> ORE_STAGES = new HashSet<>();
    public static Set<String> SIMPLE_IDS = new HashSet<>();

    static {
        ASSOCIATION_MAP.put(ITEM_INSTANCE.associatedType(), ITEM_INSTANCE);
        ASSOCIATION_MAP.put(DIMENSION_INSTANCE.associatedType(), DIMENSION_INSTANCE);
        ASSOCIATION_MAP.put(MOB_INSTANCE.associatedType(), MOB_INSTANCE);
        ASSOCIATION_MAP.put(STRUCTURE_INSTANCE.associatedType(), STRUCTURE_INSTANCE);
        ASSOCIATION_MAP.put(RECIPE_INSTANCE.associatedType(), RECIPE_INSTANCE);
        ASSOCIATION_MAP.put(SCREEN_INSTANCE.associatedType(), SCREEN_INSTANCE);
        ASSOCIATION_MAP.put(ORE_INSTANCE.associatedType(), ORE_INSTANCE);
        ASSOCIATION_MAP.put(PET_INSTANCE.associatedType(), PET_INSTANCE);
        ASSOCIATION_MAP.put(ENCHANT_INSTANCE.associatedType(), ENCHANT_INSTANCE);
        ASSOCIATION_MAP.put(CROP_INSTANCE.associatedType(), CROP_INSTANCE);
        ASSOCIATION_MAP.put(EFFECT_INSTANCE.associatedType(), EFFECT_INSTANCE);
        ASSOCIATION_MAP.put(REGION_INSTANCE.associatedType(), REGION_INSTANCE);
        ASSOCIATION_MAP.put(LOOT_INSTANCE.associatedType(), LOOT_INSTANCE);
    }

    public static void reloadBeforeScripts() {
        if (ServerLifecycleHooks.getCurrentServer() == null) { return; }

        ITEM_INSTANCE.reloadBeforeScripts();
        DIMENSION_INSTANCE.reloadBeforeScripts();
        MOB_INSTANCE.reloadBeforeScripts();
        STRUCTURE_INSTANCE.reloadBeforeScripts();
        RECIPE_INSTANCE.reloadBeforeScripts();
        SCREEN_INSTANCE.reloadBeforeScripts();
        ORE_INSTANCE.reloadBeforeScripts();
        PET_INSTANCE.reloadBeforeScripts();
        ENCHANT_INSTANCE.reloadBeforeScripts();
        CROP_INSTANCE.reloadBeforeScripts();
        EFFECT_INSTANCE.reloadBeforeScripts();
        REGION_INSTANCE.reloadBeforeScripts();
        LOOT_INSTANCE.reloadBeforeScripts();

        ALL_STAGES.clear();
        ORE_STAGES.clear();
        SIMPLE_IDS.clear();

        ANetworking.sendToAllPlayers(new RequestReloadS2CPacket(ReloadType.CLIENT_BEFORE));

        APluginManager.callMethod(AStagesPlugin::reloadBeforeScripts);

        ASimpleRestrictionManager.reloadBeforeScripts();
    }

    public static void clientSynchronization(@Nullable ServerPlayer player) {
        AStages.TIMER.start();

        ARestrictionManager.ITEM_INSTANCE.synchronizeWithClient(player);
        ARestrictionManager.RECIPE_INSTANCE.synchronizeWithClient(player);
        ARestrictionManager.MOB_INSTANCE.synchronizeWithClient(player);
        ARestrictionManager.ORE_INSTANCE.synchronizeWithClient(player);

        ANetworking.sendTo(player, new RequestReloadS2CPacket(ReloadType.CLIENT_SYNC));
        ANetworking.sendTo(player, new DimensionIdsSyncerS2CPacket(ARestrictionManager.DIMENSION_INSTANCE.getIds()));

        AStages.TIMER.stop();
        AStages.LOGGER.info("AStages synchronization took {}!", AStages.TIMER);

        APluginManager.callMethod(player, AStagesPlugin::clientSynchronization);
    }

    public static void reflectServerStagesChangesToClients(@Nullable ServerPlayer player) {
        ANetworking.sendTo(player, new ServerStagesSyncerS2CPacket(ServerStage.getServerStages(), AOperation.LOGIN));
    }

    public static void reflectSimpleIdsChangesToClients(@Nullable ServerPlayer player, Collection<String> ids, ASyncOperation operation) {
        ANetworking.sendTo(player, new SimpleIdsSyncerS2CPacket(ids, operation));
    }

    public static void reflectAllStagesChangesToClients(@Nullable ServerPlayer player, Collection<String> stages, ASyncOperation operation) {
        ANetworking.sendTo(player, new StagesSyncerS2CPacket(stages, operation));
    }

    public static void reloadAfterScripts() {
        ARestrictionManager.ITEM_INSTANCE.reloadAfterScripts();
        APluginManager.callMethod(AStagesPlugin::reloadAfterScripts);

        if (ServerLifecycleHooks.getCurrentServer() == null) { return; }
        clientSynchronization(null);
        reflectSimpleIdsChangesToClients(null, ARestrictionManager.SIMPLE_IDS, ASyncOperation.ADD);
        reflectAllStagesChangesToClients(null, ARestrictionManager.ALL_STAGES, ASyncOperation.ADD);
        APluginManager.callMethod(ServerLifecycleHooks.getCurrentServer(), AStagesPlugin::reloadAfterScripts);
        CommonEventSettings.allInventoryChanged();
    }

    @NotYetImplemented("Move to another class!")
    public static void clearClientOnLogin(ServerPlayer player) {
        ANetworking.sendToPlayer(player, new RequestReloadS2CPacket(ReloadType.CLIENT_BEFORE));
        APluginManager.callMethod(AStagesPlugin::clearClientOnLogin);
    }

    public static void addRestrictionsViaJavaCode(ARestrictionStage stage) {
        NeoForge.EVENT_BUS.post(new AddRestrictionEvent(stage));
    }

    public static void removeRestriction(String id, ARestrictionType type) {
        ASSOCIATION_MAP.get(type).removeRestriction(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable T getRestrictionById(String id, ARestrictionType type) {
        return (T) ASSOCIATION_MAP.get(type).getRestriction(id);
    }

    public static AMinimalManager<?> getManagerFromType(ARestrictionType type) {
        return ASSOCIATION_MAP.get(type);
    }

    @ForPlugins
    public static void registerManager(ARestrictionType type, AMinimalManager<?> manager) {
        EXTERNAL_MANAGERS.put(type, manager);
    }

    @ForPlugins
    public static @Nullable AMinimalManager<?> getInstance(ARestrictionType type) {
        return EXTERNAL_MANAGERS.getOrDefault(type, null);
    }

    // TODO: solve recipe restriction for survival inventory
}
