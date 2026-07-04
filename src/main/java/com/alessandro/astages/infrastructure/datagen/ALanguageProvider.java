package com.alessandro.astages.infrastructure.datagen;

import com.alessandro.astages.AStages;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ALanguageProvider extends LanguageProvider {
    public ALanguageProvider(PackOutput output, String locale) {
        super(output, AStages.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        // PLAYER STAGES
        add("message.astages.player.add", "%s unlocked!");
        add("message.astages.player.remove", "Stage %s removed!");
        add("message.astages.player.not_present", "Stages %s is not present in your stages!");
        add("message.astages.player.add_all", "All stages unlocked successfully:");
        add("message.astages.player.remove_all", "All stages removed successfully:");
        add("message.astages.player.info.no_stages", "No stages unlocked for player %s!");
        add("message.astages.player.info.has_stages", "Stages unlocked by %s:");
        add("message.astages.player.list_item", " - %s");

        // SERVER STAGES
        add("message.astages.server.add", "Server stage %s unlocked!");
        add("message.astages.server.remove", "Server stage %s removed!");
        add("message.astages.server.not_present", "Stage %s is not present in server stages!");
        add("message.astages.server.add_all", "All server stages unlocked successfully:");
        add("message.astages.server.remove_all", "All server stages removed successfully:");
        add("message.astages.server.info.no_stages", "No stages unlocked for current server!");
        add("message.astages.server.info.has_stages", "Stages unlocked in this server:");
        add("message.astages.server.list_item", " - %s");

        // TIMER & ACCESS
        add("chat.astages.timer.reset_all", "Timers and access reset correctly for restriction %s!");
        add("chat.astages.timer.reset_timer", "Timers reset correctly for restriction %s!");
        add("chat.astages.timer.reset_access", "Access reset correctly for restriction %s!");
        add("chat.astages.timer.set_timer", "Timers set correctly for restriction %s with value %s!");
        add("chat.astages.timer.set_access", "Access set correctly for restriction %s with value %s!");
        add("chat.astages.timer.increase_access", "Access increased correctly for restriction %s by value %s!");
        add("chat.astages.timer.decrease_access", "Access decreased correctly for restriction %s by value %s!");
        add("chat.astages.timer.invalid_value_access", "Invalid value for restriction %s!");

        // MODEL
        add("message.astages.missing_model.kick", "Player will be disconnected! Missing models in client: %s");
        add("message.astages.missing_model.warning", "Some models are available on client but not on server: %s");

        // SIMPLE
        add("chat.astages.simple.no_type_associated", "There is no simple restriction with id %s!");
        add("chat.astages.simple.one_type_associated", "Simple restriction with id %s removed!");
        add("chat.astages.simple.more_type_associated", "Ambiguity issue: %s is associated to more than one restriction! Provide restriction type in order to solve ambiguity.");
        add("chat.astages.simple.valid_type", "Valid types are:");
        add("chat.astages.simple.valid_type.item", "- %s");

        // ITEM
        add("tooltip.astages.item.hidden_name", "Unfamiliar Item");
        add("tooltip.astages.item.item_description", "This item has been staged to [%s]");
        add("message.astages.item.drop", "You dropped the %s.");
        add("message.astages.item.mine", "You do not know how to mine with the %s.");
        add("message.astages.item.attach", "You don't know how to attack with the %s.");
        add("message.astages.item.use", "You don't know how to use the %s.");
        add("message.astages.item.pickup", "You couldn't pick up the %s.");
        add("message.astages.item.place", "You couldn't place the %s.");
        add("tooltip.astages.item.jade_integration.item", "Unfamiliar Item");
        add("tooltip.astages.item.jade_integration.block", "Unfamiliar Block");

        // DIMENSION
        add("message.astages.dimension.enter", "You can't visit this dimension!");
        add("message.astages.dimension.leave", "You can't leave this dimension!");
        add("message.astages.dimension.expired", "Exhausted attempts to enter in this dimension!");
        add("message.astages.dimension.access.left", "You have %s access left!");
        add("message.astages.dimension.access.zero", "You can no longer enter this dimension!"); // You cannot enter this dimension anymore

        // SCREEN
        add("message.astages.screen", "You can't open this menu!");

        // PET
        add("message.astages.pet.tame", "You can't tame %s!");
        add("message.astages.pet.breed", "You can't breed %s!");
        add("message.astages.pet.mount", "You can't mount %s!");

        // STRUCTURE
        add("message.astages.structure.attack", "You can't attack in %s!");
        add("message.astages.structure.interact", "You can't interact with %s!");
        add("message.astages.structure.enter", "You can't enter in %s!");
        add("message.astages.structure.place", "You can't place in %s!");
        add("message.astages.structure.break", "You can't break in %s!");

        // MOB
        add("message.astages.mob.jade_integration", "Unknown Entity!");
        add("message.astages.mob.interaction", "You can't interact with an unknown Entity!");
        add("message.astages.mob.attack", "You can't attack an unknown Entity!");

        // REGION
        add("message.astages.region.interact", "You can't interact in this region!");
        add("message.astages.region.command", "Command disabled in this region!");

        // JADE INTEGRATION
        add("config.jade.plugin_astages.block_component_provider", "AStages");
    }
}