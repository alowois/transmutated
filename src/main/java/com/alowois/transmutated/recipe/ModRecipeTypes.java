package com.alowois.transmutated.recipe;

import com.alowois.transmutated.Transmutated;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Utility class for managing mod recipe types.
 * Handles registration of the transmutation recipe type and serializer.
 */
public class ModRecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Transmutated.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Transmutated.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, TransmutationRecipeSerializer> TRANSMUTATION_SERIALIZER =
            SERIALIZERS.register("transmutation", TransmutationRecipeSerializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<TransmutationRecipe>> TRANSMUTATION_TYPE =
            TYPES.register("transmutation", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return Transmutated.MODID + ":transmutation";
                }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
