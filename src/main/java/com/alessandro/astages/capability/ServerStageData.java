package com.alessandro.astages.capability;

import com.alessandro.astages.api.*;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.event.server.*;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.stages.ServerStagesSyncerS2CPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Contract;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Deprecated(forRemoval = true)
@NotNullParamsAndMethodsReturn
public class ServerStageData extends SavedData {
    private static final String STAGE_ID = "astages_server_stages";
    private final List<String> serverStages = new ArrayList<>();

    public List<String> getServerStages() {
        return serverStages;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasServerStage(String stage) {
        return serverStages.contains(stage);
    }

    public void addServerStage(String stage) {
        serverStages.add(stage);
        setDirty();
    }

    public void addServerStages(Set<String> stages) {
        serverStages.addAll(stages);
        setDirty();
    }

    public AStatus removeServerStage(String stage) {
        var toReturn = serverStages.remove(stage) ? AStatus.SUCCESS : AStatus.NOT_PRESENT;
        setDirty();
        return toReturn;
    }

    public AStatus removeServerStages(Set<String> stages) {
        var toReturn = serverStages.removeAll(stages) ? AStatus.SUCCESS : AStatus.NOT_PRESENT;
        setDirty();
        return toReturn;
    }

    public void synchronizeWithClient(@Nullable ServerPlayer player, AOperation operation, String stage) {
        synchronizeWithClient(player, operation, ASetUtils.singleton(stage));
    }

    public void synchronizeWithClient(@Nullable ServerPlayer player, AOperation operation, Set<String> stages) {
        AStagesUtils.checkServerStages(operation, stages);

        var server = ServerLifecycleHooks.getCurrentServer();
        var event = new StageSyncedServerEvent(ServerLifecycleHooks.getCurrentServer(), operation, stages);
        ALoader.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            ANetworking.sendTo(player, new ServerStagesSyncerS2CPacket(stages, operation));

            switch (operation) {
                case ADD -> ALoader.EVENT_BUS.post(new StageAddedServerEvent(server, ASetUtils.getOnlyElement(stages)));
                case ADD_ALL -> ALoader.EVENT_BUS.post(new AllStagesAddedServerEvent(server, stages));
                case REMOVE -> ALoader.EVENT_BUS.post(new StageRemovedServerEvent(server, ASetUtils.getOnlyElement(stages)));
                case REMOVE_ALL -> ALoader.EVENT_BUS.post(new AllStagesRemovedServerEvent(server, stages));
                case LOGIN -> ALoader.EVENT_BUS.post(new StageLoginServerEvent(server, stages));
            }
        } else {
            switch (event.getOperation()) {
                case ADD -> removeServerStage(ASetUtils.getOnlyElement(stages));
                case ADD_ALL, LOGIN -> removeServerStages(stages);
                case REMOVE -> addServerStage(ASetUtils.getOnlyElement(stages));
                case REMOVE_ALL -> addServerStages(stages);
            }
        }
    }

    @Contract(" -> new")
    public static ServerStageData create() {
        return new ServerStageData();
    }

    public static ServerStageData load(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
        var newData = create();

        var listTag = (ListTag) nbt.get("server_stages");
        if (listTag != null) {
            listTag.forEach(tag -> {
                if (tag instanceof StringTag stringTag) {
                    newData.serverStages.add(stringTag.getAsString());
                }
            });
        }

        return newData;
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registries) {
        if (serverStages.isEmpty()) { return nbt; }

        var listTag = new ListTag();
        serverStages.forEach(stage -> listTag.add(StringTag.valueOf(stage)));
        nbt.put("server_stages", listTag);

        return nbt;
    }

    public static Path getTemporaryStagesFile() {
        var file = AStagesFolderSystem.getServerTemporaryFolder().resolve("server.json");
        return AFileIOUtils.getOrCreateFile(file);
    }

    public static ServerStageData getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(ServerStageData::create, ServerStageData::load), STAGE_ID);
    }

    public static ServerStageData getData(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(ServerStageData::create, ServerStageData::load), STAGE_ID);
    }
}
