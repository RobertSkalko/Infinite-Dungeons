package com.robertx22.infinite_dungeons.database.db_types;

import com.robertx22.infinite_dungeons.db_init.RegistryTypes;
import com.robertx22.infinite_dungeons.main.InfiniteDungeonsMain;
import com.robertx22.infinite_dungeons.util.FormatUtils;
import com.robertx22.library_of_exile.registry.ExileRegistryType;
import com.robertx22.library_of_exile.registry.IAutoGson;
import com.robertx22.library_of_exile.registry.JsonExileRegistry;
import com.robertx22.library_of_exile.utils.RandomUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class RewardList implements JsonExileRegistry<RewardList>, IAutoGson<RewardList> {
    public static RewardList SERIALIZER = new RewardList();

    public int weight = 1000;
    public String id = "";
    public String format = "";

    public List<WeightedReward> rewards = new ArrayList<>();

    public WeightedReward jackpot;

    public TextFormatting getFormat() {
        return FormatUtils.of(format);
    }

    public TranslationTextComponent getTranslatable() {
        return new TranslationTextComponent(InfiniteDungeonsMain.MODID + ".reward_list." + id);
    }

    public WeightedReward getRandom() {

        List<WeightedReward> all = new ArrayList<>();
        all.addAll(rewards);
        all.add(jackpot);

        return RandomUtils.weightedRandom(all);
    }

    public List<ITextComponent> getTooltip() {

        List<ITextComponent> list = new ArrayList<>();

        list.add(new StringTextComponent(TextFormatting.GREEN + "Chance Of: "));

        for (WeightedReward reward : this.rewards) {
            list.add(reward.reward.type.getStackNameForTooltip(reward.reward));
        }

        list.add(new StringTextComponent(""));

        list.add(new StringTextComponent(TextFormatting.DARK_PURPLE + "Jackpot: "));

        list.add(jackpot.reward.type.getStackNameForTooltip(jackpot.reward));

        return list;
    }

    @Override
    public ExileRegistryType getExileRegistryType() {
        return RegistryTypes.REWARD_LIST;
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
    public Class<RewardList> getClassForSerialization() {
        return RewardList.class;
    }

}
