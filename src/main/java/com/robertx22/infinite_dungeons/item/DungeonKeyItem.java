package com.robertx22.infinite_dungeons.item;

import com.robertx22.infinite_dungeons.database.DungeonsDB;
import com.robertx22.infinite_dungeons.database.db_types.group.DungeonGroup;
import com.robertx22.infinite_dungeons.main.MainID;
import com.robertx22.library_of_exile.registry.FilterListWrap;
import com.robertx22.library_of_exile.utils.LoadSave;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class DungeonKeyItem extends Item {

    public DungeonKeyItem() {
        super(new Properties().tab(MainID.ITEM_GROUP));

    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity en, int num, boolean bool) {
        try {
            if (en.tickCount % 20 == 0) {
                if (!stack.hasTag()) {
                    KeyData data = new KeyData();
                    setData(stack, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static DungeonGroup getDungeonGroup(Item key) {
        try {
            FilterListWrap<DungeonGroup> wrap = DungeonsDB.Groups()
                .getFilterWrapped(x -> x.getKeyItem() == key);

            if (wrap.list.isEmpty()) {
                return null;
            } else {
                return wrap.random();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setData(ItemStack stack, KeyData data) {
        LoadSave.Save(data, stack.getOrCreateTag(), "dungeon");

    }

    public static KeyData getData(ItemStack stack) {
        return LoadSave.Load(KeyData.class, new KeyData(), stack.getOrCreateTag(), "dungeon");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {

        try {
            list.add(MainID.ofTranslation("key_drop"));

        } catch (Exception e) {
            // e.printStackTrace();
        }

    }

}
