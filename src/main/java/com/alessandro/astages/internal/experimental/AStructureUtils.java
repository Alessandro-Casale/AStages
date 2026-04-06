package com.alessandro.astages.internal.experimental;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AStructureUtils {
    private static final int[][] delta = {
        { -1, -1 },
        { -1, 0 },
        { -1, +1 },
        { 0, -1 },
        // { 0, 0 },
        { 0, +1 },
        { +1, -1 },
        { +1, 0 },
        { +1, +1 },
    };

    public static boolean isInsideStructure(ServerPlayer player, @NotNull List<ResourceLocation> structures) {
        return !structures.isEmpty();
    }

    public static List<VoxelShape> isCloseToStructure(ServerPlayer player, StructureManager manager, ServerLevel level) {
        var toReturn = new ArrayList<VoxelShape>();

        for (var pos : delta) {
            var deltaX = pos[0];
            var deltaZ = pos[1];
            var newPos = player.getOnPos().mutable().move(deltaX, 1, deltaZ);

            manager.getAllStructuresAt(newPos).forEach((s, longs) -> {
                var structure = level.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(s);
                if (structure != null) {
//                    var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(structure, player, player.getServer());

//                    if (restriction != null) {
                        var finalS = manager.getStructureWithPieceAt(newPos, s);
                        if (finalS.isValid()) {
                            var shape = Shapes.create(AABB.of(finalS.getBoundingBox()));
                            toReturn.add(shape);
                        }
//                    }
                }
            });
        }

        return toReturn;
    }
}
