package com.robertx22.infinite_dungeons.database.db_types;

import com.robertx22.infinite_dungeons.db_init.RegistryTypes;
import com.robertx22.infinite_dungeons.main.InfiniteDungeonsMain;
import com.robertx22.infinite_dungeons.util.FormatUtils;
import com.robertx22.library_of_exile.registry.ExileRegistryType;
import com.robertx22.library_of_exile.registry.IAutoGson;
import com.robertx22.library_of_exile.registry.JsonExileRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class DungeonDifficulty implements JsonExileRegistry<DungeonDifficulty>, IAutoGson<DungeonDifficulty> {
    public static DungeonDifficulty SERIALIZER = new DungeonDifficulty();

    public int weight = 1000;
    public String id = "";
    public String format = "";
    public int tier = 0;

    public float mob_hp_multi = 1;
    public float mob_dmg_multi = 1;
    public float coin_drop_multi = 1;

    public TextFormatting getFormat() {
        return FormatUtils.of(format);
    }

    public TranslationTextComponent getTranslatable() {
        return new TranslationTextComponent(InfiniteDungeonsMain.MODID + ".difficulty." + id);
    }

    public ResourceLocation getIconLoc() {
        return new ResourceLocation(InfiniteDungeonsMain.MODID, "textures/gui/difficulty/" + id + ".png");
    }

    public List<ITextComponent> getTooltip() {

        List<ITextComponent> list = new ArrayList<>();
        list.add(getTranslatable());

        list.add(new StringTextComponent("Mob HP: " + mob_hp_multi + "x"));
        list.add(new StringTextComponent("Mob DMG: " + mob_dmg_multi + "x"));
        list.add(new StringTextComponent("Coin Drops: " + coin_drop_multi + "x"));

        return list;
    }

    @Override
    public ExileRegistryType getExileRegistryType() {
        return RegistryTypes.DUNGEON_DIFFICULTY;
    }

    @Override
    public String GUID() {
        return id;
    }

    @Override
    public int Weight() {
        return weight;
    }

    @Override
    public Class<DungeonDifficulty> getClassForSerialization() {
        return DungeonDifficulty.class;
    }

}
