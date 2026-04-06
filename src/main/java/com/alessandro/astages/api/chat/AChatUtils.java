package com.alessandro.astages.api.chat;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@NotNullParamsAndMethodsReturn
public class AChatUtils {
    public static int INDENT_FACTOR = 2; // Equivalent in spaces for indentation
    private static int LIST_NUMBER = 0;

    public static Component copy(String value, String hover, ChatFormatting formatting) {
        return copy(Component.literal(value).withStyle(formatting), Component.literal(hover));
    }

    public static Component copy(String value, String hover) {
        return copy(Component.literal(value), Component.literal(hover));
    }

    public static Component copy(Component value, Component hover) {
        return Component.literal("") // " - "
                .withStyle(ChatFormatting.GRAY)
                .withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, value.getString())))
                .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.copy().append(" - click to copy!"))))
                .append(value);
    }

    public static Component indent(int times, Component value) {
        return Component.literal(" ".repeat(INDENT_FACTOR  * times)).append(value);
    }

    public static void setFirstListNumber(int value) {
        LIST_NUMBER = value;
    }

    public static void resetListNumber() {
        LIST_NUMBER = 1;
    }

    public static void resetListNumber(int value) {
        LIST_NUMBER = value;
    }

    public static Component listItem(Component value) {
        var toReturn = Component.literal(LIST_NUMBER + ". ").append(value);
        LIST_NUMBER++;
        return toReturn;
    }

    public static Component dashItem(Component value) {
        return dashItem(value, ChatFormatting.GRAY);
    }

    public static Component dashItem(Component value, ChatFormatting formatting) {
        return Component.literal("- ").withStyle(formatting).append(value);
    }

    public static Component emptyRow() {
        return Component.empty();
    }

    public static <T> void sendRestrictionsInfo(Player player, String title, String hover, Supplier<List<T>> supplier, Function<T, ?> function) {
        var restrictions = supplier.get();

        AChatUtils.resetListNumber();
        if (restrictions.isEmpty()) {
            player.sendSystemMessage(AChatUtils.dashItem(Component.literal(title + " (0)").withStyle(ChatFormatting.RED)));
        } else {
            player.sendSystemMessage(AChatUtils.dashItem(Component.literal(title + " (" + restrictions.size() + ")").withStyle(ChatFormatting.WHITE)));

            for (var restriction : restrictions) {
                player.sendSystemMessage(AChatUtils.indent(1, AChatUtils.listItem(AChatUtils.copy((String) function.apply(restriction), hover, ChatFormatting.GOLD))));
            }
        }
        AChatUtils.resetListNumber();
    }
}
