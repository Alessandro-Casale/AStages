package com.alessandro.astages.internal.experimental;

import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

import java.util.Arrays;
import java.util.Collection;

@UnderDevelopment
@NotNullParamsAndMethodsReturn
public abstract class StageArgumentSchema implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("test_stage_1", "test_stage_2");

    public static String getStage(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader stringReader) throws CommandSyntaxException {
        return stringReader.readString();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
