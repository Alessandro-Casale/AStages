package com.alessandro.astages;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.plugin.container.AttributeContainer;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.store.ASimpleRestrictionType;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.*;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.engine.store.ASimpleRestrictionTypes;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.engine.store.StageAttributes;
import com.alessandro.astages.infrastructure.advancement.ACriteriaTriggers;
import com.alessandro.astages.infrastructure.capability.AProvider;
import com.alessandro.astages.infrastructure.command.argument.ACommandArguments;
import com.alessandro.astages.infrastructure.config.AStagesClient;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import com.alessandro.astages.infrastructure.loot.modifier.AModifiers;
import com.alessandro.astages.infrastructure.manager.ClientManagerScanner;
import com.alessandro.astages.infrastructure.manager.ManagerScanner;
import com.alessandro.astages.infrastructure.plugin.PluginScanner;
import com.google.common.base.Stopwatch;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@NotNullParams
@Mod(AStages.MODID)
public class AStages {
    public static final String MODID = "astages";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Stopwatch TIMER = Stopwatch.createUnstarted();

    public static final Registry<Attribute<?>> ATTRIBUTES_REGISTRY = Attributes.ATTRIBUTES.makeRegistry(builder -> builder.sync(true));
    public static final Registry<ARestrictionType> RESTRICTION_TYPES_REGISTRY = ARestrictionTypes.RESTRICTION_TYPES.makeRegistry(builder -> builder.sync(true));
    public static final Registry<ASimpleRestrictionType> SIMPLE_RESTRICTION_TYPES_REGISTRY = ASimpleRestrictionTypes.SIMPLE_RESTRICTION_TYPES.makeRegistry(builder -> builder.sync(true));

    public AStages(IEventBus modEventBus, ModContainer modContainer) {
        AProvider.ATTACHMENT_TYPES.register(modEventBus);
        ACommandArguments.ARGUMENT_TYPES.register(modEventBus);
        AModifiers.MODIFIERS.register(modEventBus);
        ACriteriaTriggers.CRITERIA_TRIGGERS.register(modEventBus);

        Attributes.ATTRIBUTES.register(modEventBus);
        Attributes.Item.ATTRIBUTES.register(modEventBus);
        Attributes.Pet.ATTRIBUTES.register(modEventBus);
        Attributes.Structure.ATTRIBUTES.register(modEventBus);
        Attributes.Screen.ATTRIBUTES.register(modEventBus);
        Attributes.Dimension.ATTRIBUTES.register(modEventBus);
        Attributes.Mob.ATTRIBUTES.register(modEventBus);
        Attributes.Region.ATTRIBUTES.register(modEventBus);

        StageAttributes.ATTRIBUTES.register(modEventBus);

        ARestrictionTypes.RESTRICTION_TYPES.register(modEventBus);
        ASimpleRestrictionTypes.SIMPLE_RESTRICTION_TYPES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, AStagesCommon.SPEC, "astages/astages-common.toml");
        modContainer.registerConfig(ModConfig.Type.CLIENT, AStagesClient.SPEC, "astages/astages-client.toml");

        ManagerScanner.getAllManagers();
        ClientManagerScanner.getAllClientManagers();
        PluginScanner.getAllPlugins();

        var attributeContainer = AttributeContainer.initialize();
        PluginManager.callMethod(attributeContainer, AStagesPlugin::attachAttributes);
        var result = attributeContainer.get();
        for (var clazz : result.keySet()) {
            ARestrictionManager.ATTACHED_ATTRIBUTES.computeIfAbsent(clazz, key -> AttributeStore.builder()).combineWith(result.get(clazz));
        }

        var clientAttributeContainer = AttributeContainer.initialize();
        PluginManager.callMethod(attributeContainer, AStagesPlugin::attachClientAttributes);
        var clientResult = clientAttributeContainer.get();
        for (var clazz : clientResult.keySet()) {
            AClientRestrictionManager.ATTACHED_ATTRIBUTES.computeIfAbsent(clazz, key -> AttributeStore.builder()).combineWith(clientResult.get(clazz));
        }

        var stageAttributeContainer = AttributeContainer.initialize();
        PluginManager.callMethod(attributeContainer, AStagesPlugin::attachStageAttributes);
        var stageResult = stageAttributeContainer.get();
        for (var clazz : stageResult.keySet()) {
            AStageManager.ATTACHED_ATTRIBUTES.computeIfAbsent(clazz, key -> AttributeStore.builder()).combineWith(stageResult.get(clazz));
        }

        var clientStageAttributeContainer = AttributeContainer.initialize();
        PluginManager.callMethod(attributeContainer, AStagesPlugin::attachClientStageAttributes);
        var clientStageResult = clientStageAttributeContainer.get();
        for (var clazz : clientStageResult.keySet()) {
            AClientStageManager.ATTACHED_ATTRIBUTES.computeIfAbsent(clazz, key -> AttributeStore.builder()).combineWith(clientStageResult.get(clazz));
        }
    }

    static {
        ARestrictionManager.ITEM_INSTANCE.whiteListContainer(ChestBlockEntity.class, null);
        ARestrictionManager.ITEM_INSTANCE.whiteListContainer(CompoundContainer.class, null);
        ARestrictionManager.ITEM_INSTANCE.whiteListContainer(BarrelBlockEntity.class, null);
    }
}
