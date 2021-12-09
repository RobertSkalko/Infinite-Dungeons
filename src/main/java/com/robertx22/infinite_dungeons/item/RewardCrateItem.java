package com.robertx22.infinite_dungeons.item;

import com.robertx22.infinite_dungeons.database.DungeonsDB;
import com.robertx22.infinite_dungeons.database.db_types.RewardList;
import com.robertx22.infinite_dungeons.database.db_types.WeightedReward;
import com.robertx22.infinite_dungeons.exile_events.IDExileEvents;
import com.robertx22.infinite_dungeons.exile_events.OnItemGivenEvent;
import com.robertx22.infinite_dungeons.main.DungeonItems;
import com.robertx22.infinite_dungeons.main.MainID;
import com.robertx22.library_of_exile.utils.SoundUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class RewardCrateItem extends Item {

    public RewardCrateItem() {
        super(new Properties().tab(MainID.ITEM_GROUP)
            .stacksTo(1));
    }

    public static String TAG = "reward_list";

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> list) {
        try {
            if (this.allowdedIn(group)) {

                for (RewardList s : DungeonsDB.RewardLists()
                    .getList()) {
                    ItemStack stack = new ItemStack(DungeonItems.REWARD_CRATE.get());
                    stack.getOrCreateTag()
                        .putString(TAG, s.GUID());
                    list.add(stack);
                }
            }
        } catch (Exception e) {
            // e.printStackTrace(); why is it even being called so early?
        }

    }

    public static ItemStack create(String rewardlist) {
        ItemStack stack = new ItemStack(DungeonItems.REWARD_CRATE.get());
        stack.getOrCreateTag()
            .putString(TAG, rewardlist);
        return stack;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!world.isClientSide) {

            try {
                SoundUtils.playSound(player, SoundEvents.EXPERIENCE_ORB_PICKUP, 1, 1);

                RewardList rewardList = DungeonsDB.RewardLists()
                    .get(stack.getOrCreateTag()
                        .getString(TAG));

                WeightedReward reward = rewardList.getRandom();

                if (rewardList.jackpot.equals(reward)) {
                    SoundUtils.playSound(player, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                }

                ItemStack rewardstack = reward.reward.type.getStack(reward.reward);

                OnItemGivenEvent event = new OnItemGivenEvent(rewardstack, player).setMadeFromStack(stack);
                IDExileEvents.ON_ITEM_GIVE.callEvents(event);

                if (player.addItem(rewardstack) == false) {
                    player.spawnAtLocation(rewardstack, 1F);
                }

                stack.shrink(1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ActionResult.pass(stack);

    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        try {
            RewardList rewardList = DungeonsDB.RewardLists()
                .get(stack.getOrCreateTag()
                    .getString(TAG));

            return new TranslationTextComponent(MainID.MODID + ".reward_list." + rewardList.GUID())
                .withStyle(rewardList.getFormat(), TextFormatting.BOLD);

        } catch (Exception e) {
            //  e.printStackTrace();
        }

        return new StringTextComponent("Empty Crate");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {

        try {

            RewardList rewardList = DungeonsDB.RewardLists()
                .get(stack.getOrCreateTag()
                    .getString(TAG));

            list.addAll(rewardList.getTooltip());

        } catch (Exception e) {
            // e.printStackTrace();
        }

    }

}
