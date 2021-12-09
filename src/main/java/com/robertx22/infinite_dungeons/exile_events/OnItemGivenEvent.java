package com.robertx22.infinite_dungeons.exile_events;

import com.robertx22.library_of_exile.events.base.ExileEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class OnItemGivenEvent extends ExileEvent {

    public static String IS_PRODUCER_BOOLEAN_TAG = "is_producer";

    public ItemStack producerStack;
    public ItemStack stack;
    public PlayerEntity player;
    public LivingEntity mob;
    public BlockPos pos;

    public OnItemGivenEvent(ItemStack stack, PlayerEntity player) {
        this.stack = stack;
        this.player = player;
        this.pos = player.blockPosition();
    }

    public OnItemGivenEvent setMadeFromStack(ItemStack producer) {
        this.producerStack = producer;
        return this;
    }

    public static boolean isProducer(ItemStack stack) {
        return stack.hasTag() && stack.getOrCreateTag()
            .getBoolean(IS_PRODUCER_BOOLEAN_TAG);
    }

    public boolean hasMobSource() {
        return mob != null;
    }

}
