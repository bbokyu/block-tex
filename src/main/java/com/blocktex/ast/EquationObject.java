package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

public abstract class EquationObject extends BlockStatement {
    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return v.visitEquationObject(this, t);
    }

//    public abstract int getValue();

}
