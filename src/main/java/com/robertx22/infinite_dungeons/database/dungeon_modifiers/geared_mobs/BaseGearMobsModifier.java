package com.robertx22.infinite_dungeons.database.dungeon_modifiers.geared_mobs;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import com.robertx22.infinite_dungeons.database.db_types.dungeon_modifiers.DungeonModifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public abstract class BaseGearMobsModifier extends DungeonModifier {

    public BaseGearMobsModifier(String id) {
        super(id);
    }

    @Override
    public void onControlBlockTick(ControlBlockEntity en) {

    }

    @Override
    public void onMobSpawn(LivingEntity en) {

    }

    public void equipMobIfEmpty(LivingEntity en, EquipmentSlotType slot, ItemStack stack) {
        if (en.getItemBySlot(slot)
            .isEmpty()) {
            en.setItemSlot(slot, stack);
        }
    }

    public void disableMobGearDrops(LivingEntity en) {

        if (en instanceof MobEntity) {
            MobEntity mob = (MobEntity) en;

            mob.setDropChance(EquipmentSlotType.CHEST, 0);
            mob.setDropChance(EquipmentSlotType.FEET, 0);
            mob.setDropChance(EquipmentSlotType.LEGS, 0);
            mob.setDropChance(EquipmentSlotType.HEAD, 0);
            mob.setDropChance(EquipmentSlotType.MAINHAND, 0);
            mob.setDropChance(EquipmentSlotType.OFFHAND, 0);
        }

    }
}
