package com.alessandro.astages.engine.store;

import com.alessandro.astages.api.constant.ACompareCondition;
import com.alessandro.astages.api.store.AttributeType;
import com.google.common.reflect.TypeToken;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import java.util.function.Supplier;

public class AttributeTypes {
    public static final AttributeType<Boolean> BOOLEAN = AttributeType.create(Boolean.class);
    public static final AttributeType<Integer> INTEGER = AttributeType.create(Integer.class);
    public static final AttributeType<ResourceLocation> RESOURCE_LOCATION = AttributeType.create(ResourceLocation.class);
    public static final AttributeType<ACompareCondition> COMPARE_CONDITION = AttributeType.create(ACompareCondition.class);
    public static final AttributeType<ChatFormatting> CHAT_FORMATTING = AttributeType.create(ChatFormatting.class);
    public static final AttributeType<Component> COMPONENT = AttributeType.create(Component.class);
    public static final AttributeType<ItemStack> ITEM_STACK = AttributeType.create(ItemStack.class);

    public static final AttributeType<EntityType<?>> ENTITY = AttributeType.create(new TypeToken<>() { });

    public static final AttributeType<Function<ItemStack, Component>> STACK_TO_COMPONENT = AttributeType.create(new TypeToken<>() { });
    public static final AttributeType<Function<Entity, Component>> ENTITY_TO_COMPONENT = AttributeType.create(new TypeToken<>() { });
    public static final AttributeType<Function<String, Component>> STRING_TO_COMPONENT = AttributeType.create(new TypeToken<>() { });
    public static final AttributeType<Supplier<Component>> VOID_TO_COMPONENT = AttributeType.create(new TypeToken<>() { });
    public static final AttributeType<Function<ResourceLocation, Component>> RESOURCE_LOCATION_TO_COMPONENT = AttributeType.create(new TypeToken<>() { });
    public static final AttributeType<Function<MenuType<?>, Component>> MENU_TO_COMPONENT = AttributeType.create(new TypeToken<>() { });
}
