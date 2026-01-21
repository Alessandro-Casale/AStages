package com.alessandro.astages.store;

import com.alessandro.astages.AStages;
import com.alessandro.astages.registry.AStagesRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ASimpleRestrictionTypes {
    public static final DeferredRegister<ASimpleRestrictionType> SIMPLE_RESTRICTION_TYPES = ASimpleRestrictionType.setCurrentDeferredRegister(DeferredRegister.create(AStagesRegistries.Keys.SIMPLE_RESTRICTION_TYPES, AStages.MODID));

    public static final ASimpleRestrictionType ITEM = ASimpleRestrictionType.create("item");
    public static final ASimpleRestrictionType MOD = ASimpleRestrictionType.create("mod");
    public static final ASimpleRestrictionType DIMENSION = ASimpleRestrictionType.create("dimension");
    public static final ASimpleRestrictionType GUI = ASimpleRestrictionType.create("gui");
    public static final ASimpleRestrictionType ORE = ASimpleRestrictionType.create("ore");
    public static final ASimpleRestrictionType STRUCTURE = ASimpleRestrictionType.create("structure");
    //    public static final ASimpleRestrictionType BIOME = ASimpleRestrictionType.create("biome");
    public static final ASimpleRestrictionType TAME = ASimpleRestrictionType.create("tame");
    public static final ASimpleRestrictionType MOUNT = ASimpleRestrictionType.create("mount");
    public static final ASimpleRestrictionType RECIPE = ASimpleRestrictionType.create("recipe");
    public static final ASimpleRestrictionType ARMOR = ASimpleRestrictionType.create("armor");
}