package com.alessandro.astages.infrastructure.networking.packet.recipe;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@NotNullMethodsReturn
@Info("For now, required only by JEI.")
public record SyncRecipeS2C(String id, String stage, int priority, RecipeType<?> recipeType, List<ResourceLocation> recipes) implements AStagesPacket {
    public static final Type<SyncRecipeS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_recipe_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncRecipeS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncRecipeS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncRecipeS2C::stage,
        ByteBufCodecs.INT, SyncRecipeS2C::priority,
        ByteBufCodecs.fromCodecWithRegistries(BuiltInRegistries.RECIPE_TYPE.byNameCodec()), SyncRecipeS2C::recipeType,
        ACodecs.RESOURCE_LOCATION.apply(ByteBufCodecs.list()), SyncRecipeS2C::recipes,
        SyncRecipeS2C::new
    );

    public SyncRecipeS2C(@NotNull ARecipeRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getPriority(), restriction.getType(), restriction.getRecipes());
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientRecipeRestriction(id, stage)
                .setPriority(priority);

        for (ResourceLocation recipe : recipes) {
            restriction.restrict(new RecipeWrapper(recipeType, recipe));
        }

        AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
