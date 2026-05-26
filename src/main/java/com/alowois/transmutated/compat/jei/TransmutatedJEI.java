package com.alowois.transmutated.compat.jei;

import com.alowois.transmutated.Transmutated;
import com.alowois.transmutated.block.ModBlocks;
import com.alowois.transmutated.recipe.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * JEI plugin for the Transmutated mod.
 * Registers the transmutation recipe category, recipes, and catalysts for JEI integration.
 */
@JeiPlugin
public class TransmutatedJEI implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Transmutated.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new TransmutationCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        net.minecraft.client.multiplayer.ClientLevel level = net.minecraft.client.Minecraft.getInstance().level;
        if (level != null) {
            registration.addRecipes(TransmutationCategory.TYPE,
                    level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.TRANSMUTATION_TYPE.get())
                            .stream()
                            .map(net.minecraft.world.item.crafting.RecipeHolder::value)
                            .toList());
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ENCASED_TRANSMUTATION_SHAFT.get()), TransmutationCategory.TYPE);
    }
}
