package com.robertx22.infinite_dungeons.database.db_types.group;

import com.robertx22.library_of_exile.registry.IWeighted;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class CoinDrop implements IWeighted {

    public String item_id;
    public int weight;
    public int difficulty_tier_req = 0;

    public CoinDrop(Item item, int weight, int difficulty_tier_req) {
        this.item_id = ForgeRegistries.ITEMS.getKey(item)
            .toString();
        this.weight = weight;
        this.difficulty_tier_req = difficulty_tier_req;
    }

    public Item getCoinItem() {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(item_id));
    }

    private CoinDrop() {
    }

    @Override
    public int Weight() {
        return weight;
    }
}
