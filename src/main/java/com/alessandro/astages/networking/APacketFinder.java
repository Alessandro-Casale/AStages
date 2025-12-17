package com.alessandro.astages.networking;

import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.nullability.NotNullParams;

@UnderDevelopment("Register automatically packets using this finder and @PacketInfo(direction = NetworkDirection.PLAY_TO_CLIENT)")
@NotNullParams
public class APacketFinder {
//    public static void getAndRegisterAllPackets() {
//        var type = Type.getType(AStagesPacket.class);
//
//        for (ModFileScanData scanData : ModList.get().getAllScanData()) {
//            for (ModFileScanData.ClassData classData : scanData.getClasses()) {
//                if (classData.parent().equals(type)) {
//
//                }
//            }
//        }
//    }
//
//    private static <T extends AStagesPacket> void registerPacket(SimpleChannel networkInstance, Class<T> packetClass, int id, NetworkDirection direction) {
//        networkInstance.messageBuilder(packetClass, id, direction)
//            .decoder(buf -> {
//                try {
//                    return packetClass.getConstructor(FriendlyByteBuf.class).newInstance(buf);
//                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
//                         InvocationTargetException exception) {
//                    throw new RuntimeException("Missing FriendlyByteBuf in " + packetClass + "!");
//                }
//            })
//            .encoder(AStagesPacket::toBytes)
//            .consumerMainThread(AStagesPacket::handle)
//            .add();
//    }
}
