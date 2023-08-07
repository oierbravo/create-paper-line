package com.oierbravo.create_paper_line.compat.kubejs;

import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface DryerRecipeSchema {
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<OutputFluid> RESULT = FluidComponents.OUTPUT.key("result");
    RecipeKey<Integer> PROCESSING_TIME = NumberComponent.INT.key("processingTime").optional(100);

    public class DryingRecipeJS extends RecipeJS{

    }
    RecipeSchema SCHEMA = new RecipeSchema(DryingRecipeJS.class, DryingRecipeJS::new, RESULT, INGREDIENT, PROCESSING_TIME);

}
