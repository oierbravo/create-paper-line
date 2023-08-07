package com.oierbravo.create_paper_line.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.oierbravo.create_paper_line.CreatePaperLine;
import com.oierbravo.create_paper_line.content.machines.dryer.DryingRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class DryingCategory extends CreateRecipeCategory<DryingRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(CreatePaperLine.MODID, "drying");
    public DryingCategory(Info<DryingRecipe> info) {
        super(info);
    }


    public void setRecipe(IRecipeLayoutBuilder builder, DryingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 30).setBackground(getRenderedSlot(), -1, -1).addIngredients(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT,  130,29)
                .setBackground(getRenderedSlot(), -1, -1)
                .addItemStack(recipe.getResultItem());

    }


    public void draw(DryingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(matrixStack, 70, 32); //Output arrow
        drawProcessingTime(recipe, matrixStack, 81,49);

    }
    protected void drawProcessingTime(DryingRecipe recipe, PoseStack poseStack, int x, int y) {
        int processingTime = recipe.getProcessingTime();
        if (processingTime > 0) {
            int cookTimeSeconds = processingTime / 20;
            MutableComponent timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            fontRenderer.draw(poseStack, timeString, x, y, 0xFF808080);
        }
    }

}
