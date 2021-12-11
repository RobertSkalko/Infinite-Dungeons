package com.robertx22.infinite_dungeons.database.dungeon_types;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import com.robertx22.infinite_dungeons.database.CompletitionScore;
import com.robertx22.infinite_dungeons.database.db_types.DungeonType;
import com.robertx22.infinite_dungeons.database.db_types.layout.config.SlaughterDungeonConfig;
import com.robertx22.infinite_dungeons.database.ids.DungeonModifierIds;
import com.robertx22.infinite_dungeons.database.ids.DungeonTypeIds;
import com.robertx22.library_of_exile.utils.RandomUtils;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SlaughterDungeon extends DungeonType {

    public SlaughterDungeon() {
        super(DungeonTypeIds.SLAUGHTER);
    }

    @Override
    public void onControlBlockTick(ControlBlockEntity en) {

        SlaughterDungeonConfig config = en.data.getDungeonLayout().slaughter_config;

        if (en.data.mobs_spawned < config.total_mobs) {
            if (en.getAllDungeonMobsAlive()
                .size() < config.max_mobs_at_once) {

                int spawnmobseveryticks = config.spawn_mobs_every_x_sec * 20;

                if (en.data.hasModifier(DungeonModifierIds.FASTER_MOB_SPAWNING)) {
                    spawnmobseveryticks = 2;
                }

                if (en.data.ticks % spawnmobseveryticks == 0) {
                    boolean horde = RandomUtils.roll(config.horde_chance);

                    int tospawn = config.mobs_per_spawn;

                    if (horde) {
                        tospawn *= config.horde_multi;
                        en.sendTitleMessage(new StringTextComponent(TextFormatting.RED + " A Horde Approaches."));
                    }

                    for (int i = 0; i < tospawn; i++) {
                        en.spawnMob();
                        if (en.data.mobs_spawned % config.modifier_every_x_mobs == 0) {
                            en.data.tryAddRandomModifier(en);
                        }
                    }
                }
            }
        }

        if (en.data.ticks % 50 == 0) {
            en.sendTitleMessage(new StringTextComponent(TextFormatting.GREEN + "" + en.data.mobs_spawned + "/" + config.total_mobs), STitlePacket.Type.ACTIONBAR);

            if (en.data.mobs_spawned >= config.total_mobs && en.getAllDungeonMobsAlive()
                .isEmpty()) {
                CompletitionScore score = en.data.getDungeonLayout()
                    .getScore(en);
                en.spawnRewardsAndFinish(score);
            }
        }

    }

}

