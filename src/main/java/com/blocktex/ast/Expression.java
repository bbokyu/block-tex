package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

public abstract class Expression extends Node{
    
    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return v.visitExpression(this, t);
    }

    public abstract Expression copy();
}
