package com.alowois.transmutated.compat.jei;

import com.alowois.transmutated.Transmutated;
import com.alowois.transmutated.block.ModBlocks;
import com.alowois.transmutated.recipe.TransmutationRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;

/**
 * JEI recipe category for Transmutation.
 * Defines the layout and rendering of transmutation recipes in JEI.
 */
public class TransmutationCategory implements IRecipeCategory<TransmutationRecipe> {
    /**
     * The unique recipe type for transmutation.
     */
    public static final RecipeType<TransmutationRecipe> TYPE = RecipeType.create(Transmutated.MODID, "transmutation", TransmutationRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final IDrawable slot;

    /**
     * Constructor for TransmutationCategory.
     * Initializes the background, icon, and localized name for the category.
     *
     * @param guiHelper Helper for creating JEI GUI elements.
     */
    public TransmutationCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(120, 45);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.TRANSMUTATION_CASING.get()));
        this.localizedName = Component.translatable("block.transmutated.transmutation_casing");
        this.slot = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<TransmutationRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(TransmutationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;

        // Draw arrow separator
        String arrowText = ">";
        guiGraphics.drawString(font, arrowText, 74 - font.width(arrowText) / 2, 10, 0xFF404040, false);

        // Draw "Alt." label for the filter slot
        Component alterationText = Component.translatable("transmutated.gui.jei.alteration");
        guiGraphics.drawString(font, alterationText, 14 - font.width(alterationText) / 2, 26, 0xFF404040, false);


        // Draw success percentage if not 100%
        if (recipe.getSuccessPercentage() < 1.0f) {
            String successText = (int) (recipe.getSuccessPercentage() * 100) + "%";
            guiGraphics.drawString(font, successText, 104 - font.width(successText) / 2, 26, 0xFF404040, false);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TransmutationRecipe recipe, IFocusGroup focuses) {
        // Define the filter slot
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 5)
                .addIngredients(recipe.getAlteration().orElse(Ingredient.EMPTY))
                .setBackground(slot, -1, -1);

        // Define the input slot
        builder.addSlot(RecipeIngredientRole.INPUT, 35, 5)
                .addIngredients(recipe.getInput())
                .setBackground(slot, -1, -1);

        // Define the output slot
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 5)
                .addItemStack(recipe.getResult())
                .setBackground(slot, -1, -1);
    }
}
