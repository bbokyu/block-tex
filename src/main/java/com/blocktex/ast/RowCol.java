package com.blocktex.ast;

public class RowCol extends Expression {

    private final String text;

    public RowCol(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    // @Override
    // public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
    //     return v.visit(this, t);
    // }

    @Override
    public RowCol copy() {
        return new RowCol(this.text);
    }
}
