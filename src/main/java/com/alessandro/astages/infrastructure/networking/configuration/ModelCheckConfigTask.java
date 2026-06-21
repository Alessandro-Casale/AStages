package com.alessandro.astages.infrastructure.networking.configuration;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.infrastructure.networking.packet.reload.SendServerModelsS2C;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;

import java.util.function.Consumer;

@NotNullParamsAndMethodsReturn
public record ModelCheckConfigTask() implements ICustomConfigurationTask {
    public static final Type TYPE = new Type(AResourceLocation.fromNamespaceAndPath("model_check_task"));

    @Override
    public void run(Consumer<CustomPacketPayload> sender) {
        sender.accept(new SendServerModelsS2C());
    }

    @Override
    public Type type() {
        return TYPE;
    }
}
