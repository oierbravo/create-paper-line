package com.oierbravo.create_paper_line.compat.kubejs;

import com.oierbravo.create_paper_line.content.machines.dryer.DryingRecipe;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;

public class KubeJSPlugin extends dev.latvian.mods.kubejs.KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(DryingRecipe.Serializer.ID, DryerRecipeSchema.SCHEMA);
    }

}