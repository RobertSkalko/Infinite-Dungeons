package com.robertx22.infinite_dungeons.main;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class MainID {

    public static boolean RUN_DEV_TOOLS() {
        return false;
    }

    public static final String MODID = "infinite_dungeons";

    public static ItemGroup ITEM_GROUP = new ItemGroup(MODID + ".creative_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(DungeonItems.VOID_KEY.get());
        }
    };

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static TranslationTextComponent ofTranslation(String path) {
        return new TranslationTextComponent(MODID + "." + path);
    }

}
