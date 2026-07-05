package com.alessandro.astages.internal.legacy.block;

import com.alessandro.astages.api.nullability.NotNullParams;

@Deprecated(forRemoval = true)
//@Mod.EventBusSubscriber(modid = AStages.MODID)
@NotNullParams
public class ServerEventHandler {
//    public static int[][] delta = new int[][] {
//        { 0, 1, 1 },
//        { 0, 1, 1 },
//        { 1, 1, 0 },
//        { -1, 1, 0 },
//    };
//
//    private static int tick = 0;
//
////    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        var level = event.player.level();
//        if (level.isClientSide && event.phase == TickEvent.Phase.END) { return; }
//        var player = event.player;
//
//        tick++;
//        if (tick > 20) {
//            tick = 0;
//        }
//
//        if (tick == 20) {
//            if (level instanceof ServerLevel serverLevel) {
//                var manager = serverLevel.structureManager();
//                Structure structure = manager.registryAccess().registryOrThrow(Registries.STRUCTURE).get(AResourceLocation.parse("minecraft:pillager_outpost"));
//
//                for (UUID uuid : StructureServerEvents.playerIsInStructure.keySet()) {
////                    if (player.getUUID().equals(uuid) && StructureServerEvents.playerIsInStructure.get(uuid)) {
//                        var s = manager.getStructureAt(player.getOnPos(), structure);
//
//                        if (s != null && s.isValid() && !s.getPieces().isEmpty()) {
//    //                        s.getPieces().forEach(piece -> {
//    //                            p
//    //                        });
//
//    //                        for (var d : delta) {
//    //                            var aabb = s.getBoundingBox();
//    //                            var pos = player.getOnPos().mutable();
//    //                            pos.setX(pos.getX() + d[0]);
//    //                            pos.setY(pos.getY() + d[1]);
//    //                            pos.setZ(pos.getZ() + d[2]);
//
//
//                            s.getPieces().forEach(piece -> {
//                                var minX = piece.getBoundingBox().minX();
//                                var maxX = piece.getBoundingBox().maxX();
//                                var minY = piece.getBoundingBox().minY();
//                                var maxY = piece.getBoundingBox().maxY();
//                                var minZ = piece.getBoundingBox().minZ();
//                                var maxZ = piece.getBoundingBox().maxZ();
//
//                                for (int x = minX; x <= maxX; x++) {
//                                    for (int y = minY; y <= maxY; y++) {
//                                        for (int z = minZ; z <= maxZ; z++) {
//    //                                        var pos = new BlockPos(x, y, z);
//
//                                            if (x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ) {
//                                                serverLevel.sendParticles((ServerPlayer) player, ParticleTypes.COMPOSTER.getType(), false, x, y, z, 1, Math.random(), Math.random(), Math.random(), 0);
//                                            }
//
//                                            // /particle composter ~ ~2 ~ 1 1 1 .2 100
//    //                                        if (level.getBlockState(pos).is(Blocks.AIR)) {
//    //                                            level.setBlock(pos, ModBlocks.LIMIT_MOVEMENT_BLOCK.get().defaultBlockState(), Block.UPDATE_ALL);
//    //                                        }
//                                        }
//                                    }
//                                }
//                            });
//
//
//    //                            var bool = aabb.isInside(pos.getX(), pos.getY(), pos.getZ());
//
//    //                            if (bool) {
//    //                                level.setBlock(pos, ModBlocks.LIMIT_MOVEMENT_BLOCK.get().defaultBlockState(), Block.UPDATE_ALL);
//    //                            }
//
//    //                            AStages.LOGGER.debug(String.valueOf(bool));
//    //                        }
//                        }
////                    }
//                }
//            }
//        }
////
////        var x = event.player.position().x;
////        var y = event.player.position().y;
////        var z = event.player.position().z;
////
////        var border = level.getWorldBorder();
////        border.getCollisionShape();
////
////        var difference = Math.abs(((int) x) - x);
////        AStages.LOGGER.debug(String.valueOf(difference));
////        if (difference >= 0.6) {
////            level.setBlock(event.player.blockPosition().mutable().move(1, 0, 0), ModBlocks.LIMIT_MOVEMENT_BLOCK.get().defaultBlockState(), Block.UPDATE_ALL);
////        }
//    }
}
