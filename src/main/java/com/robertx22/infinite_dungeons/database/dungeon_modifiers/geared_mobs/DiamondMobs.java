package com.robertx22.infinite_dungeons.database.dungeon_modifiers.geared_mobs;

import com.robertx22.infinite_dungeons.database.db_types.dungeon_modifiers.ModifierType;
import com.robertx22.infinite_dungeons.database.ids.DungeonModifierIds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class DiamondMobs extends BaseGearMobsModifier {

    public DiamondMobs() {
        super(DungeonModifierIds.DIAMOND_MOBS);
        this.weight = 200;
    }

    @Override
    public ModifierType getModifierType() {
        return ModifierType.gearedMobs(2);
    }

    @Override
    public void onMobSpawn(LivingEntity en) {

        if (en instanceof MobEntity) {
            disableMobGearDrops(en);
        }

        equipMobIfEmpty(en, EquipmentSlotType.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
        equipMobIfEmpty(en, EquipmentSlotType.FEET, new ItemStack(Items.DIAMOND_BOOTS));
        equipMobIfEmpty(en, EquipmentSlotType.HEAD, new ItemStack(Items.DIAMOND_HELMET));
        equipMobIfEmpty(en, EquipmentSlotType.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));

        equipMobIfEmpty(en, EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
    }
}
