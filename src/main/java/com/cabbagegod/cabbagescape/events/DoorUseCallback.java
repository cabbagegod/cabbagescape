package com.cabbagegod.cabbagescape.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public interface DoorUseCallback {

    Event<DoorUseCallback> EVENT = EventFactory.createArrayBacked(DoorUseCallback.class,
            (listeners) -> (player, pos) -> {
                for(DoorUseCallback listener : listeners){
                    listener.interact(player, pos);
                }
    });

    void interact(PlayerEntity player, BlockPos pos);
}
