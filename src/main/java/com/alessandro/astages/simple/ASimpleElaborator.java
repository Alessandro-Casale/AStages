package com.alessandro.astages.simple;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.ASetUtils;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.command.argument.AStagesSimpleRestrictionTypeArgument;
import com.alessandro.astages.command.argument.AStagesSimpleRestrictionsIdsArgument;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.*;
import com.alessandro.astages.core.server.restriction.item.AItemModRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.registry.AStagesRegistries;
import com.alessandro.astages.store.ASimpleRestrictionType;
import com.alessandro.astages.store.ASimpleRestrictionTypes;
import com.alessandro.astages.store.Attributes;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;

@NotNullParams
public class ASimpleElaborator {
    public static void elaborateItem(ASimpleRestriction simple, boolean markAsDirty) {
        var restriction = new AItemRestriction(simple.id, simple.stage).restrict(Objects.requireNonNull(BuiltInRegistries.ITEM.get(AResourceLocation.parse(simple.object))));
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
        if (markAsDirty ) { restriction.markAsDirty(); }

        commonOperations(simple);
    }

    public static void elaborateMod(ASimpleRestriction simple, boolean markAsDirty) {
        var restriction = new AItemModRestriction(simple.id, simple.stage).restrict(simple.object);
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
        if (markAsDirty ) { restriction.markAsDirty(); }

        commonOperations(simple);
    }

    public static void elaborateDimension(ASimpleRestriction simple, boolean ignoredMarkAsDirty) {
        ARestrictionManager.DIMENSION_INSTANCE.addRestriction(new ADimensionRestriction(simple.id, simple.stage).restrict(AResourceLocation.parse(simple.object)));

        commonOperations(simple);
    }

    public static void elaborateGui(ASimpleRestriction simple, boolean ignoredMarkAsDirty) {
        ARestrictionManager.SCREEN_INSTANCE.addRestriction(new AScreenRestriction(simple.id, simple.stage).restrict(Objects.requireNonNull(BuiltInRegistries.MENU.get(AResourceLocation.parse(simple.object)))));

        commonOperations(simple);
    }

    public static void elaborateOre(ASimpleRestriction simple, boolean markAsDirty) {
        String[] splice = simple.object.split("//");
        BlockState original = Objects.requireNonNull(BuiltInRegistries.BLOCK.get(AResourceLocation.parse(splice[0]))).defaultBlockState();
        var replacement = Objects.requireNonNull(BuiltInRegistries.BLOCK.get(AResourceLocation.parse(splice[1]))).defaultBlockState();

        if (Attributes.AFFECTS_PLAYER_ACTIONS.getDefaultValue() != null) { // Only for suppressing unboxing error
            // For backward compatibility
            boolean affectsPlayerActions;
            if (splice.length == 2) {
                affectsPlayerActions = Attributes.AFFECTS_PLAYER_ACTIONS.getDefaultValue();
            } else {
                affectsPlayerActions = Boolean.parseBoolean(splice[2]);
            }

            var restriction = new AOreRestriction(simple.id, simple.stage).restrict(new OreWrapper(original, replacement));
            ARestrictionManager.ORE_INSTANCE.addRestriction(restriction);
            if (affectsPlayerActions != Attributes.AFFECTS_PLAYER_ACTIONS.getDefaultValue()) { restriction.set(Attributes.AFFECTS_PLAYER_ACTIONS, affectsPlayerActions); }
            if (markAsDirty) {
                restriction.markAsDirty();
            }
        }

        commonOperations(simple);
    }

    public static void elaborateStructure(ASimpleRestriction simple, boolean ignoredMarkAsDirty) {
        ARestrictionManager.STRUCTURE_INSTANCE.addRestriction(new AStructureRestriction(simple.id, simple.stage).restrict(AResourceLocation.parse(simple.object)));

        commonOperations(simple);
    }

    @SuppressWarnings("unused")
    public static void elaborateBiome(ASimpleRestriction simple, boolean ignoredMarkAsDirty) {
        throw new UnsupportedOperationException("Biome elaboration not supported! Id of Restriction not allowed: " + simple.id + ".");
    }

    public static void elaborateTame(ASimpleRestriction simple, boolean ignoredMarkAsDirty) {
        ARestrictionManager.PET_INSTANCE.addRestriction(new APetRestriction(simple.id, simple.stage).restrict(BuiltInRegistries.ENTITY_TYPE.get(AResourceLocation.parse(simple.object))).set(Attributes.BREEDABLE, true).set(Attributes.MOUNTABLE, true).set(Attributes.TAMABLE, false));

        commonOperations(simple);
    }

    public static void elaborateMount(ASimpleRestriction simple, boolean ignoredMarkAsDirty) {
        ARestrictionManager.PET_INSTANCE.addRestriction(new APetRestriction(simple.id, simple.stage).restrict(BuiltInRegistries.ENTITY_TYPE.get(AResourceLocation.parse(simple.object))).set(Attributes.BREEDABLE, true).set(Attributes.MOUNTABLE, false).set(Attributes.TAMABLE, true));

        commonOperations(simple);
    }

    public static void elaborateRecipe(ASimpleRestriction simple, boolean ignoredMarkAsDirty) {
        String[] splice = simple.object.split("//");
        var type = BuiltInRegistries.RECIPE_TYPE.get(AResourceLocation.parse(splice[0]));
        var id = AResourceLocation.parse(splice[1]);
        ARestrictionManager.RECIPE_INSTANCE.addRestriction(new ARecipeRestriction(simple.id, simple.stage).restrict(new RecipeWrapper(type, id)));

        commonOperations(simple);
    }

    public static void elaborateArmor(ASimpleRestriction simple, boolean markAsDirty) {
        var restriction = new AItemRestriction(simple.id, simple.stage);
        restriction.restrict(Objects.requireNonNull(BuiltInRegistries.ITEM.get(AResourceLocation.parse(simple.object))));
        restriction.setArmorAttributes();

        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
        if (markAsDirty) { restriction.markAsDirty(); }

        commonOperations(simple);
    }

    public static int commandItem(CommandContext<CommandSourceStack> c) {
        return addRestrictionForType(c.getSource().getPlayer(), ASimpleRestrictionTypes.ITEM, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(ItemArgument.getItem(c, "item").getItem())).toString());
    }

    public static int commandMod(CommandContext<CommandSourceStack> c) {
        return addRestrictionForType(c.getSource().getPlayer(), ASimpleRestrictionTypes.MOD, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), StringArgumentType.getString(c, "mod"));
    }

    public static int commandDimension(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        return addRestrictionForType(c.getSource().getPlayer(), ASimpleRestrictionTypes.DIMENSION, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), DimensionArgument.getDimension(c, "dimension").dimension().location().toString());
    }

    public static int commandGui(CommandContext<CommandSourceStack> c) {
        return addRestrictionForType(c.getSource().getPlayer(), ASimpleRestrictionTypes.GUI, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), StringArgumentType.getString(c, "gui"));
    }

    public static int commandOreWithDefaultValue(CommandContext<CommandSourceStack> c) {
        return addRestrictionForType(c.getSource().getPlayer(), ASimpleRestrictionTypes.ORE, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"),
            Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(BlockStateArgument.getBlock(c, "original").getState().getBlock())) +
                "//" +
                Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(BlockStateArgument.getBlock(c, "replacement").getState().getBlock())) +
                "//" +
                Attributes.AFFECTS_PLAYER_ACTIONS.getDefaultValue()
        );
    }

    public static int commandOre(CommandContext<CommandSourceStack> c) {
        return addRestrictionForType(c.getSource().getPlayer(), ASimpleRestrictionTypes.ORE, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"),
            Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(BlockStateArgument.getBlock(c, "original").getState().getBlock())) +
                "//" +
                Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(BlockStateArgument.getBlock(c, "replacement").getState().getBlock())) +
                "//" +
                BoolArgumentType.getBool(c, "affects_player_actions")
        );
    }

    @SuppressWarnings("all")
    public static int commandStructure(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        var player = c.getSource().getPlayer();
        if (player == null) { return 0; }
        var server = c.getSource().getPlayer().getServer();
        if (server == null) { return 0; }
        var level = server.getLevel(c.getSource().getPlayer().level().dimension());
        if (level == null) { return 0; }

        var structureId = level.registryAccess().registry(Registries.STRUCTURE).get().getHolderOrThrow(ResourceKeyArgument.getStructure(c, "structure").getKey()).getKey().registry();
        return addRestrictionForType(c.getSource().getPlayer(), ASimpleRestrictionTypes.STRUCTURE, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), structureId.toString());
    }

    public static int commandBiome(CommandContext<CommandSourceStack> ignoredC) {
        throw new UnsupportedOperationException("Biome elaboration not supported!");
    }

    public static int commandTame(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        return addRestrictionForType(c.getSource().getPlayer(), ASimpleRestrictionTypes.TAME, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(ResourceArgument.getSummonableEntityType(c, "tame").value())).toString());
    }

    public static int commandMount(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        return addRestrictionForType(c.getSource().getPlayer(), ASimpleRestrictionTypes.MOUNT, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(ResourceArgument.getSummonableEntityType(c, "mount").value())).toString());
    }

    public static int commandRecipe(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        return addRestrictionForType(c.getSource().getPlayer(), ASimpleRestrictionTypes.RECIPE, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"),
            Objects.requireNonNull(Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(ResourceLocationArgument.getRecipe(c, "recipe").value().getType())).toString()) +
                "//" +
                Objects.requireNonNull(ResourceLocationArgument.getRecipe(c, "recipe").id().toString())
        );
    }

    public static int commandArmor(CommandContext<CommandSourceStack> c) {
        return addRestrictionForType(c.getSource().getPlayer(), ASimpleRestrictionTypes.ARMOR, StringArgumentType.getString(c, "id"), StringArgumentType.getString(c, "stage"), Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(ItemArgument.getItem(c, "item").getItem())).toString());
    }

    private static int addRestrictionForType(@Nullable ServerPlayer player, ASimpleRestrictionType type, String id, String stage, String object) {
        ASimpleRestrictionManager.addRestriction(type, "simple/" + id, stage, object);
        commonCommandOperations(id);

        if (player != null) {
            player.sendSystemMessage(Component.literal("Successfully added restriction with id " + id + " and type " + type + "!").withStyle(ChatFormatting.GREEN));
        }

        return 1;
    }

    public static int removeRestriction(CommandContext<CommandSourceStack> c) {
        ASimpleRestrictionManager.removeRestriction("simple/" + AStagesSimpleRestrictionsIdsArgument.getSimpleRestrictionId(c, "id"), AStagesSimpleRestrictionTypeArgument.getType(c, "type"));

        return 1;
    }

    public static int removeRestrictionNoTypeDefined(CommandContext<CommandSourceStack> c) {
        var rawId = AStagesSimpleRestrictionsIdsArgument.getSimpleRestrictionId(c, "id");
        var id = "simple/" + rawId;
        var associatedTypes = ASimpleRestrictionManager.getAssociatedTypes(id);
        var player = c.getSource().getPlayer();

        if (associatedTypes.isEmpty()) {
            if (player != null) { player.sendSystemMessage(Component.translatable("chat.astages.simple.no_type_associated", rawId).withStyle(ChatFormatting.RED)); }
            return 0;
        } else if (associatedTypes.size() == 1) {
            if (player != null) { player.sendSystemMessage(Component.translatable("chat.astages.simple.one_type_associated", rawId).withStyle(ChatFormatting.GREEN)); }
            ASimpleRestrictionManager.removeRestriction(id, ASetUtils.getOnlyElement(associatedTypes));

            return 1;
        } else {
            if (player != null) {
                player.sendSystemMessage(Component.translatable("chat.astages.simple.more_type_associated", rawId).withStyle(ChatFormatting.RED));
                player.sendSystemMessage(Component.translatable("chat.astages.simple.valid_type").withStyle(ChatFormatting.RED));

                for (var type : associatedTypes) {
                    var resourceLocation = AStagesRegistries.SIMPLE_RESTRICTION_TYPES.getKey(type);
                    player.sendSystemMessage(Component.translatable("chat.astages.simple.valid_type.item", resourceLocation).withStyle(ChatFormatting.RED));
                }
            }
            return 0;
        }
    }

    public static void commonOperations(ASimpleRestriction simple) {
        ARestrictionManager.SIMPLE_IDS.add(simple.id.substring(7)); // Remove simple/ marker!
    }

    public static void commonCommandOperations(String id) {
        ARestrictionManager.reflectSimpleIdsChangesToClients(null, List.of(id), ASyncOperation.ADD);
    }
}
