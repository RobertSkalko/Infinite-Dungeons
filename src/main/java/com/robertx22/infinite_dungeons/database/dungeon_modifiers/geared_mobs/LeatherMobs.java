package com.robertx22.infinite_dungeons.database.dungeon_modifiers.geared_mobs;

import com.robertx22.infinite_dungeons.database.db_types.dungeon_modifiers.ModifierType;
import com.robertx22.infinite_dungeons.database.ids.DungeonModifierIds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class LeatherMobs extends BaseGearMobsModifier {

    public LeatherMobs() {
        super(DungeonModifierIds.LEATHER_MOBS);
    }

    @Override
    public ModifierType getModifierType() {
        return ModifierType.gearedMobs(0);
    }

    @Override
    public void onMobSpawn(LivingEntity en) {

        if (en instanceof MobEntity) {
            disableMobGearDrops(en);
        }

        equipMobIfEmpty(en, EquipmentSlotType.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
        equipMobIfEmpty(en, EquipmentSlotType.FEET, new ItemStack(Items.LEATHER_BOOTS));
        equipMobIfEmpty(en, EquipmentSlotType.HEAD, new ItemStack(Items.LEATHER_HELMET));
        equipMobIfEmpty(en, EquipmentSlotType.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));

        equipMobIfEmpty(en, EquipmentSlotType.MAINHAND, new ItemStack(Items.WOODEN_SWORD));

    }
}
