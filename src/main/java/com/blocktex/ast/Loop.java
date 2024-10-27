package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

public class Loop extends Statement {
    private final Name matrixName;

    private final Block block;

    public Loop(Name matrixName, Block block) {
        this.matrixName = matrixName;
        this.block = block;
    }

    public Name getName() {
        return this.matrixName;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return v.visitLoop(this, t);
    }
}
