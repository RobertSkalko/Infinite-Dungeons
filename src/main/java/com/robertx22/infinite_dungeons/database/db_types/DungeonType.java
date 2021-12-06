package com.robertx22.infinite_dungeons.database.db_types;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import com.robertx22.infinite_dungeons.db_init.RegistryTypes;
import com.robertx22.infinite_dungeons.main.MainID;
import com.robertx22.library_of_exile.registry.ExileRegistry;
import com.robertx22.library_of_exile.registry.ExileRegistryType;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class DungeonType implements ExileRegistry<DungeonType> {

    public int weight = 1000;
    public String id = "";

    public TranslationTextComponent getTranslatableDesc() {
        return new TranslationTextComponent(MainID.MODID + ".dungeon_type." + id);
    }

    public abstract void onControlBlockTick(ControlBlockEntity en);

    public DungeonType(String id) {
        this.id = id;
    }

    @Override
    public ExileRegistryType getExileRegistryType() {
        return RegistryTypes.DUNGEON_TYPE;
    }

    @Override
    public String GUID() {
        return id;
    }

    @Override
    public int Weight() {
        return weight;
    }

}
