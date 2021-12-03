package com.robertx22.infinite_dungeons.database.dungeon_modifiers.geared_mobs;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import com.robertx22.infinite_dungeons.database.db_types.dungeon_modifiers.DungeonModifier;
import com.robertx22.infinite_dungeons.database.db_types.dungeon_modifiers.ModifierType;
import com.robertx22.library_of_exile.utils.RandomUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class MeleeSkeletonChance extends DungeonModifier {

    public int chance;

    public MeleeSkeletonChance(String id, int chance) {
        super(id);
        this.chance = chance;
        this.weight = 0;
    }

    @Override
    public ModifierType getModifierType() {
        return ModifierType.meleeSkeletons(0);
    }

    @Override
    public void onControlBlockTick(ControlBlockEntity en) {

    }

    @Override
    public void onMobSpawn(LivingEntity en) {
        if (RandomUtils.roll(chance)) {
            if (en.getType() == EntityType.SKELETON) {
                en.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
        }
    }
}
