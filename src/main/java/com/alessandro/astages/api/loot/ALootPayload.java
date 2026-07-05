package com.alessandro.astages.api.loot;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@NotNullMethodsReturn
public class ALootPayload {
    private @Nullable ResourceLocation lootTable;
    private @Nullable Player player;
    private @Nullable EntityType<?> entityType;
    private @Nullable Vec3 position;
    private @Nullable DamageType damageType;
    private @Nullable BlockState blockState;

    private ALootPayload() { }

    public static ALootPayload create() {
        return new ALootPayload();
    }

    public ALootPayload lootTable(ResourceLocation lootTable) {
        this.lootTable = lootTable;
        return this;
    }

    public ALootPayload player(Player player) {
        this.player = player;
        return this;
    }

    public ALootPayload position(Vec3 position) {
        this.position = position;
        return this;
    }

    public ALootPayload entityType(EntityType<?> entityType) {
        this.entityType = entityType;
        return this;
    }

    public ALootPayload damageType(DamageType damageType) {
        this.damageType = damageType;
        return this;
    }

    public ALootPayload blockState(BlockState blockState) {
        this.blockState = blockState;
        return this;
    }

    public @Nullable ResourceLocation lootTable() {
        return lootTable;
    }

    public @Nullable Player player() {
        return player;
    }

    public @Nullable Vec3 position() {
        return position;
    }

    public @Nullable BlockPos blockPos() {
        if (position == null) { return null; }
        return BlockPos.containing(position);
    }

    public @Nullable EntityType<?> entityType() {
        return entityType;
    }

    public @Nullable DamageType damageType() {
        return damageType;
    }

    public @Nullable BlockState blockState() {
        return blockState;
    }
}