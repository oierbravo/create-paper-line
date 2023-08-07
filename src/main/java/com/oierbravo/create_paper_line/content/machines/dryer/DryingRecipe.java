package com.oierbravo.create_paper_line.content.machines.dryer;

import com.google.gson.JsonObject;
import com.oierbravo.create_paper_line.CreatePaperLine;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class DryingRecipe implements Recipe<SimpleContainer>, IRecipeTypeInfo {
    ResourceLocation id;
    private Ingredient ingredient;
    private ItemStack result;
    private int processingTime;

    DryingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, int processingTime){
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.processingTime = processingTime;
    }
    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return ingredient.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        pContainer.removeItem(0,1);
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return result.copy();
    }

    public int getProcessingTime(){
        return processingTime;
    }
    public Ingredient getIngredient(){
        return ingredient;
    }
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<DryingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "drying";
    }
    public static class Serializer implements RecipeSerializer<DryingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(CreatePaperLine.MODID,"drying");

        @Override
        public DryingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));

            int processingTime = GsonHelper.getAsInt(json, "processingTime",1);

            return new DryingRecipe(id, ingredient, result, processingTime);
        }

        @Override
        public DryingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();


            int processingTime = buffer.readInt();

            return new DryingRecipe(id, ingredient, result, processingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DryingRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeItem(recipe.getResultItem());
            buffer.writeInt(recipe.getProcessingTime());
        }


    }
}
