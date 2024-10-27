package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

public class SetLoopElement extends BlockStatement{

    private final Expression expression;

    public SetLoopElement(Expression ex) {
        this.expression = ex;
    }


    public Expression getExpression() {
        return expression;
    }


    public <T, U> U accept(BlockTexVisitor<T, U> v, T t, int i, int j, MatrixObject m) {
        return v.visitSetLoopElement(this, t, i, j, m);
    }
}
