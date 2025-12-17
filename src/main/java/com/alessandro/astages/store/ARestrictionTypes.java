package com.alessandro.astages.store;

import com.alessandro.astages.AStages;
import com.alessandro.astages.registry.AStagesRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ARestrictionTypes {
    public static final DeferredRegister<ARestrictionType> RESTRICTION_TYPES = ARestrictionType.setCurrentDeferredRegister(DeferredRegister.create(AStagesRegistries.Keys.RESTRICTION_TYPES, AStages.MODID));

    public static final ARestrictionType ITEM = ARestrictionType.create("item");
    public static final ARestrictionType MOB = ARestrictionType.create("mob");
    public static final ARestrictionType DIMENSION = ARestrictionType.create("dimension");
    public static final ARestrictionType STRUCTURE = ARestrictionType.create("structure");
    public static final ARestrictionType RECIPE = ARestrictionType.create("recipe");
    public static final ARestrictionType SCREEN = ARestrictionType.create("screen");
    public static final ARestrictionType ORE = ARestrictionType.create("ore");
    public static final ARestrictionType PET = ARestrictionType.create("pet");
    public static final ARestrictionType ENCHANT = ARestrictionType.create("enchant");
    public static final ARestrictionType CROP = ARestrictionType.create("crop");
    public static final ARestrictionType EFFECT = ARestrictionType.create("effect");
    public static final ARestrictionType REGION = ARestrictionType.create("region");
    public static final ARestrictionType LOOT = ARestrictionType.create("loot");
}
