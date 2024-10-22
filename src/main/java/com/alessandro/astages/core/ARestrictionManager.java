package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import com.alessandro.astages.util.ARestrictionType;
import mekanism.common.recipe.MekanismRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ARestrictionManager {
    public static final AItemManager ITEM_INSTANCE = new AItemManager();
    public static final ADimensionManager DIMENSION_INSTANCE = new ADimensionManager();
    public static final AMobManager MOB_INSTANCE = new AMobManager();
    public static final ATimeManager TIME_INSTANCE = new ATimeManager();
    public static final AStructureManager STRUCTURE_INSTANCE = new AStructureManager();
    public static final ARecipeManager RECIPE_INSTANCE = new ARecipeManager();
    public static final AScreenManager SCREEN_INSTANCE = new AScreenManager();
    public static final AOreManager ORE_INSTANCE = new AOreManager();

    public static final Set<String> ALL_STAGES = new HashSet<>();

//    static {
//        var rest = new ARecipeRestriction("id_auto");
//        rest.type = RecipeType.SMELTING;
//        rest.restrict(new ResourceLocation("minecraft", "charcoal"));
//
//        var rest1 = new ARecipeRestriction("id_auto_1");
//        rest1.type = MekanismRecipeType.CRUSHING.getRecipeType();
//        rest1.restrict(new ResourceLocation("mekanism", "crushing/gravel_to_sand"));
//
//        RECIPE_INSTANCE.addRestriction("test_auto", rest);
//        RECIPE_INSTANCE.addRestriction("test_auto_mod", rest1);
//    }

    @SuppressWarnings("unchecked")
    public static <T extends ARestriction> T getRestrictionById(@NotNull ARestrictionType type, String id) {

//        if (type == ARestrictionType.DIMENSION) {
//            return (T) DIMENSION_INSTANCE.getRestriction(id);
//        }
//
//        return (T) new AItemRestriction(id);

        return switch (type) {
            case ITEM -> (T) ITEM_INSTANCE.getRestriction(id);
            case MOB -> (T) MOB_INSTANCE.getRestriction(id);
            case DIMENSION -> (T) DIMENSION_INSTANCE.getRestriction(id);
            case TIME -> (T) TIME_INSTANCE.getRestriction(id);
            case STRUCTURE -> (T) STRUCTURE_INSTANCE.getRestriction(id);
            case RECIPE -> (T) RECIPE_INSTANCE.getRestriction(id);
            case SCREEN -> (T) SCREEN_INSTANCE.getRestriction(id);
            case ORE -> (T) ORE_INSTANCE.getRestriction(id);
        };
    }
}
