package com.oierbravo.create_paper_line.registrate;

import com.oierbravo.create_paper_line.CreatePaperLine;
import com.oierbravo.create_paper_line.content.machines.dryer.DryingRecipe;
import com.oierbravo.create_paper_line.foundatation.utility.ModLang;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CreatePaperLine.MODID);

    public static final RegistryObject<RecipeSerializer<DryingRecipe>> DRYING_SERIALIZER =
            SERIALIZERS.register("dryer", () -> DryingRecipe.Serializer.INSTANCE);


    private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, CreatePaperLine.MODID);

    public static final RegistryObject<RecipeType<?>> DRYING_TYPE = TYPES.register( ModLang.asId("drying"), () -> simpleType(CreatePaperLine.asResource("drying")));

    public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
        String stringId = id.toString();
        return new RecipeType<T>() {
            @Override
            public String toString() {
                return stringId;
            }
        };
    }
    public static void register(IEventBus eventBus) {

        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
    public static Optional<DryingRecipe> findDrying(ItemStack input, Level level){
        if(level.isClientSide())
            return Optional.empty();
        return level.getRecipeManager().getRecipeFor(DryingRecipe.Type.INSTANCE, new SimpleContainer(input), level );
    }
}
