package com.alessandro.astages.infrastructure.capability;

import com.alessandro.astages.AStages;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class AProvider {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, AStages.MODID);

    @SuppressWarnings("removal")
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerStage>> PLAYER_STAGE = ATTACHMENT_TYPES.register(
        "player_stage", () -> AttachmentType.serializable(PlayerStage::new).build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BlockStage>> BLOCK_STAGE = ATTACHMENT_TYPES.register(
        "block_stage", () -> AttachmentType.serializable(BlockStage::new).build()
    );
}
