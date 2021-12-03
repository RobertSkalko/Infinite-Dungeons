package com.robertx22.infinite_dungeons.events;

import com.robertx22.infinite_dungeons.components.MobIDCap;
import com.robertx22.infinite_dungeons.configs.IDConfig;
import com.robertx22.infinite_dungeons.database.DungeonsDB;
import com.robertx22.infinite_dungeons.database.db_types.group.DungeonGroup;
import com.robertx22.library_of_exile.events.base.EventConsumer;
import com.robertx22.library_of_exile.events.base.ExileEvents;
import com.robertx22.library_of_exile.utils.RandomUtils;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class OnMobKill extends EventConsumer<ExileEvents.OnMobDeath> {

    @Override
    public void accept(ExileEvents.OnMobDeath event) {

        if (MobIDCap.get(event.mob)
            .isDungeonMob()) {

            MobIDCap.get(event.mob)
                .getControlBlock()
                .ifPresent(x -> x.data.getModifiers()
                    .forEach(m -> m.onMobDeath(event)));

        }

        if (event.killer instanceof PlayerEntity) {

            if (event.mob instanceof MobEntity || event.mob instanceof IMob || !event.mob.getType()
                .getCategory()
                .isFriendly()) {

                boolean validKill = ExileEvents.IS_KILLED_ENTITY_VALID.callEvents(new ExileEvents.IsEntityKilledValid(event.mob, event.killer)).isValid;
                // Age of Exile can call and say " you killed mob too low level, false! "

                // todo need better spawn mechanics

                // only non dungeon mobs can drop keys
                if (!MobIDCap.get(event.mob)
                    .isDungeonMob()) {
                    if (validKill && RandomUtils.roll(IDConfig.get().KEY_DROP_CHANCE.get())) {
                        DungeonGroup group = DungeonsDB.Groups()
                            .random();
                        ItemStack stack = new ItemStack(group.getKeyItem());
                        event.mob.spawnAtLocation(stack);
                    }
                }

                try {

                    if (validKill) {
                        MobIDCap.get(event.mob)
                            .getControlBlock()
                            .ifPresent(x -> {

                                float chance = 5 * x.data.getDifficulty().coin_drop_multi;

                                if (RandomUtils.roll(chance)) {
                                    ItemStack stack = new ItemStack(RandomUtils.weightedRandom(x.data.getDungeonLayout()
                                            .getGroup().coin_drops)
                                        .getCoinItem());
                                    event.mob.spawnAtLocation(stack);
                                }
                            });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
