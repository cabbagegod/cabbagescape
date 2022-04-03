package com.cabbagegod.cabbagescape.blockoutline;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockPosState {
    BlockPos pos;
    BlockState state;

     public BlockPosState(BlockPos pos, BlockState state){
         this.pos = pos;
         this.state = state;
     }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }
}
