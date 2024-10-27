package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;
// Credit: General Structure from TinyVars Conditional Class
public class Conditional extends BlockStatement {


    private final Condition condition;
    private final Block ifBlock;
    private final Block elseBlock;

    public Conditional(Condition condition, Block ifBlock, Block elseBlock) {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public Condition getCondition() {
        return condition;
    }

    public Block getIfBlock() {
        return ifBlock;
    }

    public Block getElseBlock() {
        return elseBlock;
    }


    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t, int i, int j, MatrixObject mo) {
        return v.visitConditional(this, t, i, j, mo);
    }

    // @Override
    // public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
    //     return v.visitConditional(this, t);
    // }
}
