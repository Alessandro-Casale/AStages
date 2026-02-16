package com.alessandro.astages.networking.packet.recipe;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.recipe.AClientRecipeRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.networking.ACodes;
import com.alessandro.astages.networking.AStagesPacket;
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
public record RecipeSyncerS2CPacket(String id, String stage, int priority, RecipeType<?> recipeType, List<ResourceLocation> recipes) implements AStagesPacket {
    public static final Type<RecipeSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("recipe_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, RecipeSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, RecipeSyncerS2CPacket::stage,
        ByteBufCodecs.INT, RecipeSyncerS2CPacket::priority,
        ByteBufCodecs.fromCodecWithRegistries(BuiltInRegistries.RECIPE_TYPE.byNameCodec()), RecipeSyncerS2CPacket::recipeType,
        ACodes.RESOURCE_LOCATION.apply(ByteBufCodecs.list()), RecipeSyncerS2CPacket::recipes,
        RecipeSyncerS2CPacket::new
    );

    public RecipeSyncerS2CPacket(@NotNull ARecipeRestriction restriction) {
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
