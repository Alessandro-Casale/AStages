package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.develop.Info;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class StageDisplay {
    public Component addTitle;
    public Component removeTitle;
    public Component addSubTitle;
    public Component removeSubTitle;
    public Component addChatMessage;
    public Component removeChatMessage;
    public int fadeIn = 20;
    public int fadeOut = 20;
    public int stay = 60;

    @Info("For visualization representation") public ItemStack stack;
    public boolean hasCustomStack = false;
}
