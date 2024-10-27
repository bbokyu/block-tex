package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

// Credit: General Structure from TinyVars Conditional Class
public class Condition extends Node {

    private final Expression left;
    private final Expression right;

    private final String condition;

    public Condition(Expression left, Expression right, String condition) {
        this.left = left;
        this.right = right;
        this.condition = condition;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public String getCondition() {
        return condition;
    }

    public <T, U> U accept(BlockTexVisitor<T, U> v, T t, int i, int j, MatrixObject mo) {
        return v.visitCondition(this, t, i, j, mo);
    }


    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return null;
    }
}
