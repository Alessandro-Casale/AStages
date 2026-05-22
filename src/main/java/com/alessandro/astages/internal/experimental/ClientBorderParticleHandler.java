package com.alessandro.astages.internal.experimental;

// @EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ClientBorderParticleHandler {
//
//    // @SubscribeEvent
//    public static void onPlayerTick(PlayerTickEvent.Post event) {
//        Player player = event.getEntity();
//        Level level = player.level();
//
//        // Eseguiamo solo lato client e solo per il giocatore che stiamo controllando noi
//        // (ignora gli altri giocatori in multiplayer)
//        if (!level.isClientSide || player != Minecraft.getInstance().player) {
//            return;
//        }
//
//        AABB playerBox = player.getBoundingBox();
//        RandomSource random = player.getRandom();
//
//        // Distanza di attivazione: le particelle appaiono se sei entro 2.5 blocchi dalla barriera
//        double interactionDistance = 5D;
//
//        for (AABB box : StructureCollisionHelper.CLIENT_RESTRICTED_BOXES) {
//            // Un controllo rapido per vedere se il giocatore è abbastanza vicino
//            if (box.inflate(interactionDistance).intersects(playerBox)) {
//                spawnBarrierParticles(player, box, random, level, interactionDistance);
//            }
//        }
//    }
//
//    private static void spawnBarrierParticles(Player player, AABB box, RandomSource random, Level level, double maxDistance) {
//        // Quante particelle generare per tick (più alto = barriera più densa)
//        int particlesPerTick = 10;
//
//        for (int i = 0; i < particlesPerTick; i++) {
//            // 1. Scegliamo un punto casuale in una sfera attorno al giocatore
//            double offsetX = (random.nextDouble() - 0.5) * (maxDistance * 2);
//            double offsetY = (random.nextDouble() - 0.5) * (maxDistance * 2);
//            double offsetZ = (random.nextDouble() - 0.5) * (maxDistance * 2);
//
//            double targetX = player.getX() + offsetX;
//            double targetY = player.getY() + 1.0 + offsetY; // Centrato sull'altezza degli occhi
//            double targetZ = player.getZ() + offsetZ;
//
//            // 2. Usiamo Mth.clamp per "incollare" questo punto alla superficie della Bounding Box
//            double closestX = Mth.clamp(targetX, box.minX, box.maxX);
//            double closestY = Mth.clamp(targetY, box.minY, box.maxY);
//            double closestZ = Mth.clamp(targetZ, box.minZ, box.maxZ);
//
//            // 3. Verifichiamo che il punto calcolato sia effettivamente sulla FACCIA esterna della box
//            // (e non dentro il volume)
//            boolean isOnEdge = closestX == box.minX || closestX == box.maxX ||
//                closestY == box.minY || closestY == box.maxY ||
//                closestZ == box.minZ || closestZ == box.maxZ;
//
//            // 4. Se è sul bordo, e la distanza reale dal giocatore è entro il limite, spawniamo la particella
//            if (isOnEdge && player.distanceToSqr(closestX, closestY, closestZ) < (maxDistance * maxDistance)) {
//
//                // Genera la particella.
//                // ParticleTypes.WITCH crea delle stelline viola magiche.
//                // Puoi provare anche ParticleTypes.SOUL_FIRE_FLAME (fuoco blu) o ParticleTypes.SMOKE (fumo).
//                level.addParticle(
//                    ParticleTypes.WITCH,
//                    closestX, closestY, closestZ,
//                    0.0D, 0.0D, 0.0D // Velocità X, Y, Z della particella
//                );
//            }
//        }
//    }
}