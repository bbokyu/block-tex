package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

public class BlockStatement extends Statement {


    public <T, U> U accept(BlockTexVisitor<T, U> v, T t, int i, int j, MatrixObject mo) {
        return v.visitBlockStatement(this, t, i, j, mo);
    }

    // @Override
    // public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
    //     return v.visitBlockStatement(this, t);
    // }
}
