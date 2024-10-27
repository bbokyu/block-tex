package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

public class ArithmeticExpression extends Expression {
    private final Expression left;
    private final Expression right;
    private final String operator; // For simplicity, using a String. Could be an enum.

    public ArithmeticExpression(Expression left, Expression right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public String getOperator() {
        return operator;
    }


    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return v.visitArithmeticExpression(this, t);
    }

    @Override
    public ArithmeticExpression copy() {
        Expression leftCopy = left.copy();
        Expression rightCopy = right.copy();

        return new ArithmeticExpression(leftCopy, rightCopy, operator);
    }

}
