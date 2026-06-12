package com.alessandro.astages.infrastructure.capability;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class PlacedBlocksInStructureData extends SavedData {
    private static final String STRUCTURE_ID = "astages_structure_data_";
    private final HashSet<BlockPos> placedByPlayerBlocks = new HashSet<>(); // StructureStart: Block Positions

    public static PlacedBlocksInStructureData create() {
        return new PlacedBlocksInStructureData();
    }

    public boolean isBlockPlacedByPlayer(BlockPos blockPos) {
        return placedByPlayerBlocks.contains(blockPos);
    }

    public void add(BlockPos... pos) {
        placedByPlayerBlocks.addAll(List.of(pos));
        setDirty();
    }

    public void remove(BlockPos... pos) {
        List.of(pos).forEach(placedByPlayerBlocks::remove);
        setDirty();
    }

    public static PlacedBlocksInStructureData load(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
        var newData = create();

        var listTag = (ListTag) nbt.get("structure_blocks");
        if (listTag != null) {
            listTag.forEach(tag -> {
                if (tag instanceof LongTag longTag) {
                    newData.placedByPlayerBlocks.add(BlockPos.of(longTag.getAsLong()));
                }
            });
        }

        return newData;
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registries) {
        var listTag = new ListTag();
        placedByPlayerBlocks.forEach(blockPos -> listTag.add(LongTag.valueOf(blockPos.asLong())));
        nbt.put("structure_blocks", listTag);

        return nbt;
    }

    public static PlacedBlocksInStructureData getData(ServerLevel level, String structureId) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(PlacedBlocksInStructureData::create, PlacedBlocksInStructureData::load), STRUCTURE_ID + structureId.replace(':', '_'));
    }

    public static PlacedBlocksInStructureData getData(MinecraftServer server, String structureId) {
        return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(PlacedBlocksInStructureData::create, PlacedBlocksInStructureData::load), STRUCTURE_ID + structureId.replace(':', '_'));
    }
}
