package com.robertx22.infinite_dungeons.database.db_types.layout;

import com.robertx22.infinite_dungeons.main.DungeonItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ShopCost {

    public String item_id = "";
    public int count = 1;

    public ShopCost(String item_id, int count) {
        this.item_id = item_id;
        this.count = count;
    }

    public static ShopCost skeletalCoins(int count) {
        return new ShopCost(ForgeRegistries.ITEMS.getKey(DungeonItems.SKELETAL_COIN.get())
            .toString(), count);
    }

    public static ShopCost netherCoins(int count) {
        return new ShopCost(ForgeRegistries.ITEMS.getKey(DungeonItems.NETHER_COIN.get())
            .toString(), count);
    }

    public static ShopCost voidCoins(int count) {
        return new ShopCost(ForgeRegistries.ITEMS.getKey(DungeonItems.VOID_KEY.get())
            .toString(), count);
    }

    public static ShopCost of(Item item, int count) {
        return new ShopCost(ForgeRegistries.ITEMS.getKey(item)
            .toString(), count);
    }

    public boolean isItem(Item item) {
        return item == getItem();
    }

    public boolean canPay(PlayerEntity player) {
        Item item = getItem();
        return player.inventory.countItem(item) >= count;
    }

    public void pay(PlayerEntity player) {
        Item item = getItem();

        int costToPay = this.count;

        for (int j = 0; j < player.inventory.getContainerSize(); ++j) {
            if (costToPay < 1) {
                break;
            }

            ItemStack stack = player.inventory.getItem(j);
            if (stack.getItem()
                .equals(item)) {
                int paid = MathHelper.clamp(stack.getCount(), 0, costToPay);
                costToPay -= paid;
                stack.shrink(paid);
            }
        }

    }

    public ItemStack getShowStack() {
        return new ItemStack(getItem(), count);
    }

    public Item getItem() {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(item_id));
    }
}
