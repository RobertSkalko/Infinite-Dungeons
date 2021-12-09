package com.robertx22.infinite_dungeons.exile_events;

import com.robertx22.infinite_dungeons.database.db_types.DungeonDifficulty;
import com.robertx22.infinite_dungeons.database.db_types.layout.DungeonLayout;
import com.robertx22.library_of_exile.events.base.ExileEvent;
import net.minecraft.entity.player.PlayerEntity;

public class TryStartDungeonEvent extends ExileEvent {

    public PlayerEntity player;
    public DungeonLayout layout;
    public DungeonDifficulty difficulty;

    public boolean canStart = true;

    public TryStartDungeonEvent(PlayerEntity player, DungeonLayout layout, DungeonDifficulty difficulty) {
        this.player = player;
        this.layout = layout;
        this.difficulty = difficulty;
    }

    public void doNotAllowStartingDungeon() {
        this.canStart = false;
    }

}
