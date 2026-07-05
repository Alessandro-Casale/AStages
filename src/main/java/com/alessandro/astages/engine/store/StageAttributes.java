package com.alessandro.astages.engine.store;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.stage.event.ExpiredEvent;
import com.alessandro.astages.api.stage.event.GrantedEvent;
import com.alessandro.astages.api.stage.event.TickEvent;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.infrastructure.registry.AStagesRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
import java.util.function.Function;

public class StageAttributes {
    public static final DeferredRegister<Attribute<?>> ATTRIBUTES = Attribute.setCurrentDeferredRegister(DeferredRegister.create(AStagesRegistries.Keys.ATTRIBUTES, AStages.MODID));

    public static final Attribute<Boolean> PLAYER_ONLY = Attribute.create("stages_player_only", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> SERVER_ONLY = Attribute.create("stages_server_only", AttributeTypes.BOOLEAN, false);
    public static final Attribute<Boolean> SHOW_DEFAULT = Attribute.create("stages_show_default", AttributeTypes.BOOLEAN, true);

    public static final Attribute<Function<String, Component>> TITLE_ADD = Attribute.create("stages_title_add", AttributeTypes.STRING_TO_COMPONENT, null);
    public static final Attribute<Function<String, Component>> TITLE_REMOVE = Attribute.create("stages_title_remove", AttributeTypes.STRING_TO_COMPONENT, null);
    public static final Attribute<Function<String, Component>> SUBTITLE_ADD = Attribute.create("stages_subtitle_add", AttributeTypes.STRING_TO_COMPONENT, null);
    public static final Attribute<Function<String, Component>> SUBTITLE_REMOVE = Attribute.create("stages_subtitle_remove", AttributeTypes.STRING_TO_COMPONENT, null);
    public static final Attribute<Function<String, Component>> CHAT_MESSAGE_ADD = Attribute.create("stages_chat_message_add", AttributeTypes.STRING_TO_COMPONENT, null);
    public static final Attribute<Function<String, Component>> CHAT_MESSAGE_REMOVE = Attribute.create("stages_chat_message_remove", AttributeTypes.STRING_TO_COMPONENT, null);
    public static final Attribute<Function<String, Component>> ACTION_BAR_MESSAGE_ADD = Attribute.create("stages_action_bar_message_add", AttributeTypes.STRING_TO_COMPONENT, null);
    public static final Attribute<Function<String, Component>> ACTION_BAR_MESSAGE_REMOVE = Attribute.create("stages_action_bar_message_remove", AttributeTypes.STRING_TO_COMPONENT, null);

    public static final Attribute<Integer> FADE_IN = Attribute.create("stages_fade_in", AttributeTypes.INTEGER, 20);
    public static final Attribute<Integer> FADE_OUT = Attribute.create("stages_fade_out", AttributeTypes.INTEGER, 20);
    public static final Attribute<Integer> STAY = Attribute.create("stages_stay", AttributeTypes.INTEGER, 60);

    public static final Attribute<ItemStack> ICON = Attribute.create("stages_icon", AttributeTypes.ITEM_STACK, null);

    public static final Attribute<Consumer<GrantedEvent>> GRANTED_EVENT = Attribute.create("stages_granted_event", StageAttributeTypes.GRANTED_EVENT, null);
    public static final Attribute<Consumer<TickEvent>> TICK_EVENT = Attribute.create("stages_tick_event", StageAttributeTypes.TICK_EVENT, null);
    public static final Attribute<Consumer<ExpiredEvent>> EXPIRED_EVENT = Attribute.create("stages_expired_event", StageAttributeTypes.EXPIRED_EVENT, null);
}