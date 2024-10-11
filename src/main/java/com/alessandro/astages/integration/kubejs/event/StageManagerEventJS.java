package com.alessandro.astages.integration.kubejs.event;

import com.alessandro.astages.core.AItemRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.custom.InitialStageAdditionEvent;
//import com.alessandro.astages.util.RestrictionType;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.integration.forge.gamestages.GameStageEventJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class StageManagerEventJS extends EventJS {
//    public static int id = 0;
//
//    public static int id() {
//        return id++;
//    }

    InitialStageAdditionEvent event;

    public StageManagerEventJS(InitialStageAdditionEvent event) { }

//    public AItemRestriction addRestrictionForItem(String stage, Item @NotNull ... items) {
//        var restriction = new AItemRestriction(AItemRestriction.NON_RUNTIME_ID + "_" + id);
//
//        for (var item : items) {
//            restriction.restrict(itemStack -> itemStack.is(item));
//        }
//
//        ARestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);
//
//        return restriction;
//    }
}
