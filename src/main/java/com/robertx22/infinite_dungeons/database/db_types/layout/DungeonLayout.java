package com.robertx22.infinite_dungeons.database.db_types.layout;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import com.robertx22.infinite_dungeons.database.CompletitionScore;
import com.robertx22.infinite_dungeons.database.DungeonsDB;
import com.robertx22.infinite_dungeons.database.db_types.DungeonType;
import com.robertx22.infinite_dungeons.database.db_types.ShopList;
import com.robertx22.infinite_dungeons.database.db_types.group.DungeonGroup;
import com.robertx22.infinite_dungeons.database.db_types.layout.config.CleanUpDungeonConfig;
import com.robertx22.infinite_dungeons.database.db_types.layout.config.SlaughterDungeonConfig;
import com.robertx22.infinite_dungeons.database.db_types.layout.config.WaveBasedDungeonConfig;
import com.robertx22.infinite_dungeons.database.ids.DungeonGroupIds;
import com.robertx22.infinite_dungeons.database.ids.DungeonTypeIds;
import com.robertx22.infinite_dungeons.db_init.RegistryTypes;
import com.robertx22.infinite_dungeons.main.InfiniteDungeonsMain;
import com.robertx22.infinite_dungeons.util.FormatUtils;
import com.robertx22.library_of_exile.registry.ExileRegistryType;
import com.robertx22.library_of_exile.registry.IAutoGson;
import com.robertx22.library_of_exile.registry.JsonExileRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DungeonLayout implements JsonExileRegistry<DungeonLayout>, IAutoGson<DungeonLayout> {
    public static DungeonLayout SERIALIZER = new DungeonLayout();

    public int weight = 1000;
    public String id = "";
    public String format = "";

    public String dungeon_type = DungeonTypeIds.WAVE;
    public String dungeon_group = DungeonGroupIds.SKELETAL;

    public String structure_file = "";

    public List<DungeonSpawn> spawns = new ArrayList<>();

    public String shop_list = "";

    public ShopList getShopList() {
        return DungeonsDB.ShopLists()
            .get(shop_list);
    }

    public HashMap<CompletitionScore, ScoreConfig> score_configs = new HashMap<>();

    public SlaughterDungeonConfig slaughter_config = null;
    public WaveBasedDungeonConfig wave_config = null;
    public CleanUpDungeonConfig clean_up_config = null;

    public ResourceLocation getIconTextureLoc() {
        return new ResourceLocation(InfiniteDungeonsMain.MODID, "textures/gui/dungeon_icon/" + id + ".png");
    }

    public CompletitionScore getScore(ControlBlockEntity ce) {
        int sec = ce.data.ticks / 20;

        if (sec < score_configs.get(CompletitionScore.GOLD).reward_time_seconds) {
            return CompletitionScore.GOLD;
        }
        if (sec < score_configs.get(CompletitionScore.SILVER).reward_time_seconds) {
            return CompletitionScore.SILVER;
        }
        return CompletitionScore.BRONZE;
    }

    public DungeonType getDungeonType() {
        return DungeonsDB.DungeonTypes()
            .get(dungeon_type);
    }

    public DungeonGroup getGroup() {
        return DungeonsDB.Groups()
            .get(dungeon_group);
    }

    public TextFormatting getFormat() {
        return FormatUtils.of(format);
    }

    public TranslationTextComponent getTranslatable() {
        return new TranslationTextComponent(InfiniteDungeonsMain.MODID + ".layout." + id);
    }

    @Override
    public ExileRegistryType getExileRegistryType() {
        return RegistryTypes.DUNGEON_LAYOUT;
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
    public Class<DungeonLayout> getClassForSerialization() {
        return DungeonLayout.class;
    }

}
