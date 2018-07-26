package com.msvdaamen.recipe;

import com.google.gson.JsonObject;
import com.msvdaamen.init.Config;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public class disableRecipes implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        return () -> {
            return Config.useVanillaRecipe;
        };
    }


}
