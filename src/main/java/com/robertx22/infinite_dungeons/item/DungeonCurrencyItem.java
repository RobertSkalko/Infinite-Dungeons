package com.robertx22.infinite_dungeons.item;

import com.robertx22.infinite_dungeons.main.InfiniteDungeonsMain;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class DungeonCurrencyItem extends Item {

    public DungeonCurrencyItem() {
        super(new Properties().tab(InfiniteDungeonsMain.ITEM_GROUP)
            .stacksTo(64));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {

        try {
            list.add(InfiniteDungeonsMain.ofTranslation("coin_drop"));

        } catch (Exception e) {
            // e.printStackTrace();
        }

    }

}
