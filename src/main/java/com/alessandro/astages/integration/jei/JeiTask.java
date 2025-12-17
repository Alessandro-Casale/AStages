package com.alessandro.astages.integration.jei;

import com.alessandro.astages.api.develop.Info;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class JeiTask extends RecursiveTask<ArrayList<ItemStack>> {
    private static final int THRESHOLD = 500;
    private final List<ItemStack> ingredients;
    private final int start;
    private final int end;

//    public JeiTask(@NotNull Collection<ItemStack> ingredients) {
//        this.ingredients = ingredients;
//        this.start = 0;
//        this.end = ingredients.size();
//    }

    public JeiTask(@NotNull Collection<ItemStack> ingredients, int start, int end) {
        this.ingredients = ingredients.stream().toList();
        this.start = start;
        this.end = end;
    }

    @Info("Error in items selection!")
    @Override
    protected ArrayList<ItemStack> compute() {
        if (end - start < THRESHOLD) {
            var toReturn = new ArrayList<ItemStack>();

//            IntStream.range(start, end)
//                    .map(i -> {
//                        var stack = ingredients.get(i);
//
//                        return ingredients.get(i);
//                    })

            ingredients.subList(start, end).forEach(stack -> {
//                if (AClientRestrictionManager.NEW_ITEM_INSTANCE.getRestriction(stack) != null) {
//                    toReturn.add(stack);
//                }
            });

            return toReturn;
        } else {
            int mid = (start + end) / 2;
            var firstHalf = new JeiTask(ingredients, start, mid);
            firstHalf.fork();
            var secondHalf = new JeiTask(ingredients, mid, end);

            var toReturn = secondHalf.compute();
            toReturn.addAll(firstHalf.join());
            return toReturn;

//            firstHalf.fork();
//            var secondResult = secondHalf.compute();
//            var firstResult = firstHalf.join();
//
//            var result = new HashSet<>(firstResult);
//            result.addAll(secondResult);
//            return result;
        }
    }
}
