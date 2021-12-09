package com.robertx22.infinite_dungeons.database.db_types.layout;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.robertx22.infinite_dungeons.exile_events.IDExileEvents;
import com.robertx22.infinite_dungeons.exile_events.OnItemGivenEvent;
import com.robertx22.infinite_dungeons.item.RewardCrateItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

public class ShopReward {

    public enum Type {
        ITEM {
            @Override
            public ItemStack getStack(ShopReward reward) {
                ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(reward.value)), reward.count);

                if (!reward.data.isEmpty()) {
                    CompoundNBT nbt = null;
                    try {
                        nbt = new JsonToNBT(new StringReader(reward.data)).readStruct();
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                    stack.setTag(nbt);

                }
                return stack;
            }

            @Override
            public void giveReward(ShopReward reward, PlayerEntity player) {
                giveItem(getStack(reward), player);
            }
        };

        public ITextComponent getStackNameForTooltip(ShopReward reward) {

            ItemStack stack = getStack(reward);

            return new StringTextComponent(stack.getCount() + "x ").append(stack.getDisplayName());

        }

        public abstract ItemStack getStack(ShopReward reward);

        public abstract void giveReward(ShopReward reward, PlayerEntity player);

    }

    public static ShopReward item(Item item, int count) {
        return new ShopReward(ForgeRegistries.ITEMS.getKey(item)
            .toString(), Type.ITEM, count);
    }

    public static ShopReward nbtItem(ItemStack stack, int count) {
        ShopReward reward = item(stack.getItem(), count);

        // reward.data = MyGSON.GSON.toJson(stack.getTag());

        reward.data = stack.getTag()
            .toString();

        return reward;
    }

    public ShopReward(String value, Type type, int count) {
        this.value = value;
        this.type = type;
        this.count = count;
    }

    public String value = "";
    public String data = "";
    public Type type = Type.ITEM;
    public int count = 1;

    public void give(PlayerEntity player) {
        type.giveReward(this, player);
    }

    public static void giveItem(ItemStack stack, PlayerEntity player) {

        if (stack.getItem() instanceof RewardCrateItem) {
            stack.getOrCreateTag()
                .putBoolean(OnItemGivenEvent.IS_PRODUCER_BOOLEAN_TAG, true);
        }

        OnItemGivenEvent event = new OnItemGivenEvent(stack, player);
        IDExileEvents.ON_ITEM_GIVE.callEvents(event);

        if (player.addItem(stack) == false) {
            player.spawnAtLocation(stack, 1F);
        }

        player.inventory.setChanged();

    }

}
