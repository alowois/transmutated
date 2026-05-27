package com.alowois.transmutated.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.Optional;

/**
 * Serializer for the TransmutationRecipe, handling JSON and network synchronization.
 */
public class TransmutationRecipeSerializer implements RecipeSerializer<TransmutationRecipe> {
    public static final MapCodec<TransmutationRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("input").forGetter(TransmutationRecipe::getInput),
            ItemStack.CODEC.fieldOf("output").forGetter(TransmutationRecipe::getResult),
            Codec.FLOAT.optionalFieldOf("success_percentage", 1.0f).forGetter(TransmutationRecipe::getSuccessPercentage),
            Ingredient.CODEC.optionalFieldOf("alteration").forGetter(TransmutationRecipe::getAlteration)
    ).apply(inst, TransmutationRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TransmutationRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodecWithRegistries(Ingredient.CODEC), TransmutationRecipe::getInput,
            ItemStack.STREAM_CODEC, TransmutationRecipe::getResult,
            ByteBufCodecs.FLOAT, TransmutationRecipe::getSuccessPercentage,
            ByteBufCodecs.optional(ByteBufCodecs.fromCodecWithRegistries(Ingredient.CODEC)), TransmutationRecipe::getAlteration,
            TransmutationRecipe::new
    );

    @Override
    public MapCodec<TransmutationRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, TransmutationRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
