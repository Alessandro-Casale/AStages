package com.alessandro.astages.engine.store;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.constant.ACompareCondition;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.api.util.ATextUtils;
import com.alessandro.astages.infrastructure.registry.AStagesRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

// Read attributes as: can be used in/can be shown...
public class Attributes {
    public static final DeferredRegister<Attribute<?>> ATTRIBUTES = Attribute.setCurrentDeferredRegister(DeferredRegister.create(AStagesRegistries.Keys.ATTRIBUTES, AStages.MODID));

    public static final Attribute<Boolean> PICKUP = Attribute.create("pickup", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> EQUIPPING = Attribute.create("equipping", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> STORING_IN_INVENTORY = Attribute.create("storing_in_inventory", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> ATTACKING = Attribute.create("attacking", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> HIDING_RECIPE_VIEWER = Attribute.create("hiding_recipe_viewer", AttributeTypes.BOOLEAN, true);
    public static final Attribute<Boolean> BLOCK_PLACING = Attribute.create("block_placing", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> LEFT_CLICK_INTERACTIONS = Attribute.create("left_click_interactions", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> RIGHT_CLICK_INTERACTIONS = Attribute.create("right_click_interactions", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> BLOCK_BREAKING = Attribute.create("block_breaking", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> TAMING = Attribute.create("taming", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> BREEDING = Attribute.create("breeding", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> MOUNTING = Attribute.create("mounting", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> ENTERING = Attribute.create("entering", AttributeTypes.BOOLEAN, true);
    public static final Attribute<Boolean> GENERIC_INTERACTIONS = Attribute.create("generic_interactions", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> EXPLOSIONS_AFFECT_BLOCKS = Attribute.create("explosions_affect_blocks", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> EXPLOSIONS_AFFECT_ENTITIES = Attribute.create("explosions_affect_entities", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> BIDIRECTIONAL = Attribute.create("bidirectional", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> ANVIL = Attribute.create("anvil", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> ENCHANTING_TABLE = Attribute.create("enchanting_table", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> BLOCK_INTERACTIONS = Attribute.create("block_interactions", AttributeTypes.BOOLEAN, true);
    public static final Attribute<Boolean> OVERALL_MOB_SPAWNING = Attribute.create("overall_mob_spawning", AttributeTypes.BOOLEAN, true);
    public static final Attribute<Boolean> SPAWN_WITH_EQUIPMENT = Attribute.create("spawn_with_equipment", AttributeTypes.BOOLEAN, false);
    @UnderDevelopment("This attribute is not yet fully implemented and may not work as expected.") public static final Attribute<Boolean> ALLOW_ACCESS = Attribute.create("allow_access", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> PERFORM_COMMANDS = Attribute.create("perform_commands", AttributeTypes.BOOLEAN, true);
    public static final Attribute<Boolean> HAS_REPLACER = Attribute.create("has_replacer", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> HAS_CHECKER = Attribute.create("has_checker", AttributeTypes.BOOLEAN, false);
    // public static final Attribute<Boolean> HURT = Attribute.create("hurt", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> APPLY_EVERYWHERE = Attribute.create("apply_everywhere", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> STORING_IN_CONTAINERS = Attribute.create("storing_in_containers", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> AFFECTS_PLAYER_ACTIONS = Attribute.create("affects_player_actions", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> MATCH_ALL_BLOCK_STATES = Attribute.create("match_all_block_states", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> SHOW_ACTION_BAR_NAME = Attribute.create("show_action_bar_name", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> SHOW_TOOLTIP_NAME = Attribute.create("show_tooltip_name", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> SHOW_RECIPE_VIEWER_NAME = Attribute.create("show_recipe_viewer_name", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> SHOW_JADE_ITEM_NAME = Attribute.create("show_jade_item_name", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> SHOW_JADE_BLOCK_NAME = Attribute.create("show_jade_block_name", AttributeTypes.BOOLEAN, false);


    public static final Attribute<Integer> PICKUP_DELAY = Attribute.create("pickup_delay", AttributeTypes.INTEGER, 60);
    public static final Attribute<Integer> AGE = Attribute.create("age", AttributeTypes.INTEGER, null); // null
    public static final Attribute<Integer> LEVEL = Attribute.create("level", AttributeTypes.INTEGER, null); // null
    public static final Attribute<Integer> MIN_LIGHT_LEVEL = Attribute.create("min_light_level", AttributeTypes.INTEGER, null); // null
    public static final Attribute<Integer> MAX_LIGHT_LEVEL = Attribute.create("max_light_level", AttributeTypes.INTEGER, null); // null
    public static final Attribute<Integer> MAX_ACCESS = Attribute.create("max_access", AttributeTypes.INTEGER, 1);

    public static final Attribute<ResourceLocation> DIMENSION = Attribute.create("dimension", AttributeTypes.RESOURCE_LOCATION, null);

    public static final Attribute<EntityType<?>> REPLACEMENT = Attribute.create("replacement", AttributeTypes.ENTITY, null);

    public static final Attribute<ACompareCondition> COMPARE_CONDITION = Attribute.create("compare_condition", AttributeTypes.COMPARE_CONDITION, null);

    public static final Attribute<ChatFormatting> TIMER_FORMATTING = Attribute.create("timer_formatting", AttributeTypes.CHAT_FORMATTING, ChatFormatting.GOLD);

    // If the creation of subclasses is HORRIBLE, change the default value to null and request, when you add an attribute to the attribute store, to check if a new default value is set
    public static class Item {
        public static final DeferredRegister<Attribute<?>> ATTRIBUTES = Attribute.setCurrentDeferredRegister(DeferredRegister.create(AStagesRegistries.Keys.ATTRIBUTES, AStages.MODID));
        public static final Attribute<Function<ItemStack, Component>> DROP_MESSAGE = Attribute.create("item_drop_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.drop", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> ATTACK_MESSAGE = Attribute.create("item_attack_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.attack", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> PICKUP_MESSAGE = Attribute.create("item_pickup_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.pickup", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> USE_MESSAGE = Attribute.create("item_use_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.use", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> BREAKING_MESSAGE = Attribute.create("item_breaking_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.break", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> PLACE_MESSAGE = Attribute.create("item_place_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.place", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> ACTION_BAR_MESSAGE = Attribute.create("item_action_bar_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.action_bar_message", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> TOOLTIP_MESSAGE = Attribute.create("item_message_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.tooltip_message", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> RECIPE_VIEWER_MESSAGE = Attribute.create("item_recipe_viewer_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.recipe_viewer_message", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> JADE_ITEM_MESSAGE = Attribute.create("item_jade_item_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.jade_integration.item", stack.getHoverName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ItemStack, Component>> JADE_BLOCK_MESSAGE = Attribute.create("item_jade_block_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("message.astages.item.jade_integration.block", stack.getHoverName()).withStyle(ChatFormatting.RED));
    }

    public static class Pet {
        public static final DeferredRegister<Attribute<?>> ATTRIBUTES = Attribute.setCurrentDeferredRegister(DeferredRegister.create(AStagesRegistries.Keys.ATTRIBUTES, AStages.MODID));
        public static final Attribute<Function<Entity, Component>> TAME_MESSAGE = Attribute.create("pet_tame_message", AttributeTypes.ENTITY_TO_COMPONENT, entity -> Component.translatable("message.astages.pet.tame", entity.getName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<Entity, Component>> BREED_MESSAGE = Attribute.create("pet_breed_message", AttributeTypes.ENTITY_TO_COMPONENT, entity -> Component.translatable("message.astages.pet.breed", entity.getName()).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<Entity, Component>> MOUNT_MESSAGE = Attribute.create("pet_mount_message", AttributeTypes.ENTITY_TO_COMPONENT, entity -> Component.translatable("message.astages.pet.mount", entity.getName()).withStyle(ChatFormatting.RED));
    }

    public static class Structure {
        public static final DeferredRegister<Attribute<?>> ATTRIBUTES = Attribute.setCurrentDeferredRegister(DeferredRegister.create(AStagesRegistries.Keys.ATTRIBUTES, AStages.MODID));
        public static final Attribute<Function<ResourceLocation, Component>> ATTACK_MESSAGE = Attribute.create("structure_attack_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.structure.attack", ATextUtils.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ResourceLocation, Component>> INTERACT_MESSAGE = Attribute.create("structure_interact_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.structure.interact", ATextUtils.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ResourceLocation, Component>> ENTER_MESSAGE = Attribute.create("structure_enter_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.structure.enter", ATextUtils.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ResourceLocation, Component>> PLACE_MESSAGE = Attribute.create("structure_place_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.structure.place", ATextUtils.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ResourceLocation, Component>> BREAKING_MESSAGE = Attribute.create("structure_breaking_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.structure.break", ATextUtils.structureToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
    }

    public static class Screen {
        public static final DeferredRegister<Attribute<?>> ATTRIBUTES = Attribute.setCurrentDeferredRegister(DeferredRegister.create(AStagesRegistries.Keys.ATTRIBUTES, AStages.MODID));
        public static final Attribute<Function<MenuType<?>, Component>> OPEN_MESSAGE = Attribute.create("screen_open_message", AttributeTypes.MENU_TO_COMPONENT, menu -> Component.translatable("message.astages.screen.open", menu.toString()).withStyle(ChatFormatting.RED));
    }

    public static class Dimension {
        public static final DeferredRegister<Attribute<?>> ATTRIBUTES = Attribute.setCurrentDeferredRegister(DeferredRegister.create(AStagesRegistries.Keys.ATTRIBUTES, AStages.MODID));
        public static final Attribute<Function<ResourceLocation, Component>> ENTER_MESSAGE = Attribute.create("dimension_enter_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.dimension.enter", ATextUtils.dimensionToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ResourceLocation, Component>> LEAVE_MESSAGE = Attribute.create("dimension_leave_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.dimension.leave", ATextUtils.dimensionToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
        public static final Attribute<Function<ResourceLocation, Component>> EXPIRED_MESSAGE = Attribute.create("dimension_expired_message", AttributeTypes.RESOURCE_LOCATION_TO_COMPONENT, resourceLocation -> Component.translatable("message.astages.dimension.expired", ATextUtils.dimensionToDescription(resourceLocation)).withStyle(ChatFormatting.RED));
    }

    public static class Mob {
        public static final DeferredRegister<Attribute<?>> ATTRIBUTES = Attribute.setCurrentDeferredRegister(DeferredRegister.create(AStagesRegistries.Keys.ATTRIBUTES, AStages.MODID));
        public static final Attribute<Supplier<Component>> JADE_MESSAGE = Attribute.create("mob_jade_message", AttributeTypes.VOID_TO_COMPONENT, () -> Component.translatable("message.astages.mob.jade_integration").withStyle(ChatFormatting.RED));
        public static final Attribute<Supplier<Component>> INTERACTION_MESSAGE = Attribute.create("mob_interaction_message", AttributeTypes.VOID_TO_COMPONENT, () -> Component.translatable("message.astages.mob.interaction").withStyle(ChatFormatting.RED));
        public static final Attribute<Supplier<Component>> ATTACK_MESSAGE = Attribute.create("mob_attack_message", AttributeTypes.VOID_TO_COMPONENT, () -> Component.translatable("message.astages.mob.attack").withStyle(ChatFormatting.RED));
    }

    public static class Region {
        public static final DeferredRegister<Attribute<?>> ATTRIBUTES = Attribute.setCurrentDeferredRegister(DeferredRegister.create(AStagesRegistries.Keys.ATTRIBUTES, AStages.MODID));
        public static final Attribute<Supplier<Component>> INTERACT_MESSAGE = Attribute.create("region_interact_message", AttributeTypes.VOID_TO_COMPONENT, () -> Component.translatable("message.astages.region.interact").withStyle(ChatFormatting.RED));
        public static final Attribute<Function<String, Component>> COMMAND_MESSAGE = Attribute.create("region_command_message", AttributeTypes.STRING_TO_COMPONENT, performedCommand -> Component.translatable("message.astages.region.command", performedCommand).withStyle(ChatFormatting.RED));
    }
}
