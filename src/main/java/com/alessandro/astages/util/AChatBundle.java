package com.alessandro.astages.util;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.function.Supplier;

@NotNullParams
public class AChatBundle {
    private final ArrayList<AComponent> components = new ArrayList<>();
    private boolean finalized = false;
    private final String usage;
    private final Player player;

    // Add server parameter if required!
    public AChatBundle(String usage, Player player) {
        this.usage = usage;
        this.player = player;
    }

    public AChatBundle literal(String text) {
        components.add(new AComponent(Component.literal(text)));
        return this;
    }

    public AChatBundle literal(String text, ChatFormatting style) {
        components.add(new AComponent(Component.literal(text).withStyle(style)));
        return this;
    }

    public AChatBundle custom(Component component) {
        components.add(new AComponent(component));
        return this;
    }

    public AChatBundle emptyRow() {
        components.add(new AComponent(AChatUtils.emptyRow()));
        return this;
    }

    public AChatBundle dashItem(Component component) {
       components.add(new AComponent(AChatUtils.dashItem(component)));
       return this;
    }

    public AChatBundle dashItem(Component component, ChatFormatting style) {
        components.add(new AComponent(AChatUtils.dashItem(component, style)));
        return this;
    }

    public AChatBundle dashItem(String text) {
        components.add(new AComponent(AChatUtils.dashItem(Component.literal(text))));
        return this;
    }

    public AChatBundle dashItem(String text, ChatFormatting style) {
        components.add(new AComponent(AChatUtils.dashItem(Component.literal(text), style)));
        return this;
    }

    public AChatBundle build() {
        AChatUtils.resetListNumber();
        finalized = true;
        return this;
    }

    public AChatBundle printInChat() {
        return printIn(AContext.PRINT_IN_CHAT);
    }

    public AChatBundle printInLogs() {
        return printIn(AContext.PRINT_IN_LOGS);
    }

    private AChatBundle printIn(AContext initialContext) {
        for (var component : components) {
            var finalContext = component.canBePrintedInContext(initialContext);
            if (finalContext == null) { continue; }

            switch (finalContext) {
                case PRINT_IN_CHAT -> player.sendSystemMessage(component.getComponent());
                case PRINT_IN_LOGS -> AStages.LOGGER.info(component.getComponent().getString());
            }
        }

        return this;
    }

    public AChatBundle buildAndDiscriminate(boolean discriminantForPrintInLogs) {
        build();
        return discriminantForPrintInLogs ? printInLogs() : printInChat();
    }

    public static AChatBundle discriminant(boolean discriminant, Supplier<AChatBundle> ifTrue, Supplier<AChatBundle> ifFalse) {
        return discriminant ? ifTrue.get() : ifFalse.get();
    }

    public AChatBundle markAsAlwaysSendInChat() {
        lastComponentAdded().setAlwaysSendInChat(true);
        return this;
    }

    public AChatBundle markAsPrintOnlyWhenPrintInLogsIsCalled() {
        lastComponentAdded().setPrintOnlyWhenPrintInLogsIsCalled(true);
        return this;
    }

    public AChatBundle markAsPrintOnlyWhenPrintInChatIsCalled() {
        lastComponentAdded().setPrintOnlyWhenPrintInChatIsCalled(true);
        return this;
    }

    private AComponent lastComponentAdded() {
        return components.get(components.size() - 1);
    }

    public String getUsage() {
        return usage;
    }

    static class AComponent {
        private final Component component;
        private boolean alwaysSendInChat;
        private boolean printOnlyWhenPrintInLogsIsCalled;
        private boolean printOnlyWhenPrintInChatIsCalled;

        public AComponent(Component component) {
            this.component = component;
            this.alwaysSendInChat = false;
        }

        public Component getComponent() {
            return component;
        }

        public boolean isAlwaysSendInChat() {
            return alwaysSendInChat;
        }

        public boolean isPrintOnlyWhenPrintInLogsIsCalled() {
            return printOnlyWhenPrintInLogsIsCalled;
        }

        public boolean isPrintOnlyWhenPrintInChatIsCalled() {
            return printOnlyWhenPrintInChatIsCalled;
        }

        public void setAlwaysSendInChat(boolean alwaysSendInChat) {
            this.alwaysSendInChat = alwaysSendInChat;
        }

        public void setPrintOnlyWhenPrintInLogsIsCalled(boolean printOnlyWhenPrintInLogsIsCalled) {
            this.printOnlyWhenPrintInLogsIsCalled = printOnlyWhenPrintInLogsIsCalled;
        }

        public void setPrintOnlyWhenPrintInChatIsCalled(boolean printOnlyWhenPrintInChatIsCalled) {
            this.printOnlyWhenPrintInChatIsCalled = printOnlyWhenPrintInChatIsCalled;
        }

        public AContext canBePrintedInContext(AContext initialContext) {
            switch (initialContext) {
                case PRINT_IN_CHAT -> {
                    if (printOnlyWhenPrintInLogsIsCalled) {
                        return null;
                    }
                }
                case PRINT_IN_LOGS -> {
                    if (printOnlyWhenPrintInChatIsCalled) {
                        return null;
                    }

                    if (alwaysSendInChat) {
                        return AContext.PRINT_IN_CHAT;
                    }
                }
            }

            return initialContext;
        }
    }

    public enum AContext {
        PRINT_IN_CHAT, PRINT_IN_LOGS
    }
}
