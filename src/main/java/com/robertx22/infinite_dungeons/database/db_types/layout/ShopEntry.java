package com.robertx22.infinite_dungeons.database.db_types.layout;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import com.robertx22.infinite_dungeons.components.PlayerIDCap;
import com.robertx22.infinite_dungeons.database.DungeonsDB;
import com.robertx22.infinite_dungeons.database.db_types.DungeonDifficulty;
import net.minecraft.entity.player.PlayerEntity;

public class ShopEntry {

    public ShopCost cost;
    public ShopReward reward;
    public String difficulty;
    public int max_uses;

    public ShopEntry(ShopCost cost, ShopReward reward, String difficulty, int max_uses) {
        this.cost = cost;
        this.reward = reward;
        this.difficulty = difficulty;
        this.max_uses = max_uses;
    }

    public DungeonDifficulty getDifficulty() {
        return DungeonsDB.Difficulties()
            .get(difficulty);
    }

    public void buy(PlayerEntity player) {
        this.cost.pay(player);
        this.reward.give(player);
        PlayerIDCap.get(player).data.buy_history.getOrCreateHistoryOf(this).amount++;
    }

    public boolean canBuy(ControlBlockEntity be, PlayerEntity player) {

        if (!cost.canPay(player)) {
            return false;
        }

        if (PlayerIDCap.get(player).data.buy_history.getOrCreateHistoryOf(this).amount >= this.max_uses) {
            return false;
        }

        return be.data.getDifficulty().tier >= getDifficulty().tier;
    }
}
