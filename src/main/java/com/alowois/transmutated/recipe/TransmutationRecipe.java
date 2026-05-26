package com.alowois.transmutated.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A class representing a transmutation recipe.
 */
public class TransmutationRecipe implements Recipe<RecipeInput> {
    private final Ingredient input;
    private final int inputCount;
    private final ItemStack result;
    private final float successPercentage;
    private final Optional<Ingredient> alteration;

    public TransmutationRecipe(Ingredient input, int inputCount, ItemStack result, float successPercentage, Optional<Ingredient> alteration) {
        this.input = input;
        this.inputCount = inputCount;
        this.result = result;
        this.successPercentage = successPercentage;
        this.alteration = alteration;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        // Standard matching is not used as we handle it manually in the block entity
        return false;
    }

    /**
     * Checks if the given filter and input items match this recipe.
     *
     * @param filterStack The item currently in the transmutation casing's filter slot.
     * @param filterTest  A predicate to test if an item is accepted by the current filter.
     * @param inputStack  The item being processed.
     * @return true if both the filter and input match the recipe requirements.
     */
    public boolean matches(ItemStack filterStack, Predicate<ItemStack> filterTest, ItemStack inputStack) {
        if (inputStack.isEmpty()) return false;
        if (!this.input.test(inputStack)) return false;

        if (this.alteration.isPresent()) {
            if (filterStack.isEmpty()) return false;
            for (ItemStack stack : this.alteration.get().getItems()) {
                if (filterTest.test(stack)) return true;
            }
            return false;
        }
        // If no alteration is required, match if filter is empty OR if filter accepts the input item
        return filterStack.isEmpty() || filterTest.test(inputStack);
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.TRANSMUTATION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.TRANSMUTATION_TYPE.get();
    }

    public Ingredient getInput() { return input; }
    public int getInputCount() { return inputCount; }
    public ItemStack getResult() { return result; }
    public float getSuccessPercentage() { return successPercentage; }
    public Optional<Ingredient> getAlteration() { return alteration; }
}
