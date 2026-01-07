package com.alessandro.astages;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.capability.AProvider;
import com.alessandro.astages.command.argument.ACommandArguments;
import com.alessandro.astages.config.AStagesClient;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.loot.AModifiers;
import com.alessandro.astages.plugin.APluginFinder;
import com.alessandro.astages.plugin.APluginManager;
import com.alessandro.astages.plugin.AStagesPlugin;
import com.alessandro.astages.plugin.container.AttributeContainer;
import com.alessandro.astages.plugin.container.ManagerContainer;
import com.alessandro.astages.store.*;
import com.alessandro.astages.util.underdevelopment.block.ModBlocks;
import com.alessandro.astages.util.underdevelopment.item.ModItems;
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

    public AStages(IEventBus modEventBus, ModContainer modContainer) {
        AProvider.ATTACHMENT_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ACommandArguments.ARGUMENT_TYPES.register(modEventBus);
        AModifiers.MODIFIERS.register(modEventBus);

        Attributes.ATTRIBUTES.register(modEventBus);
        Attributes.Item.ATTRIBUTES.register(modEventBus);
        Attributes.Pet.ATTRIBUTES.register(modEventBus);
        Attributes.Structure.ATTRIBUTES.register(modEventBus);
        Attributes.Screen.ATTRIBUTES.register(modEventBus);
        Attributes.Dimension.ATTRIBUTES.register(modEventBus);
        Attributes.Mob.ATTRIBUTES.register(modEventBus);
        Attributes.Region.ATTRIBUTES.register(modEventBus);

        ARestrictionTypes.RESTRICTION_TYPES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, AStagesCommon.SPEC, "astages-common.toml");
        modContainer.registerConfig(ModConfig.Type.CLIENT, AStagesClient.SPEC, "astages-client.toml");

        APluginFinder.getAllPlugins();

        var managerContainer = ManagerContainer.initialize();
        APluginManager.callMethod(managerContainer, AStagesPlugin::registerManagers);
        ARestrictionManager.EXTERNAL_MANAGERS.putAll(managerContainer.get());

        var attributeContainer = AttributeContainer.initialize();
        APluginManager.callMethod(attributeContainer, AStagesPlugin::attachAttributes);
        var result = attributeContainer.get();
        for (var clazz : result.keySet()) {
            ARestrictionManager.ATTACHED_ATTRIBUTES.computeIfAbsent(clazz, key -> AttributeStore.builder()).combineWith(result.get(clazz));
        }
    }

    static {
        ARestrictionManager.ITEM_INSTANCE.whiteListContainer(ChestBlockEntity.class, null);
        ARestrictionManager.ITEM_INSTANCE.whiteListContainer(CompoundContainer.class, null);
        ARestrictionManager.ITEM_INSTANCE.whiteListContainer(BarrelBlockEntity.class, null);
    }
}
