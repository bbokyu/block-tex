package com.blocktex.ast;

import java.util.Objects;

import com.blocktex.visitor.BlockTexVisitor;

public class Name extends Expression {

    private final String text;

    public Name(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Name)) return false;

        Name name = (Name) o;

        return Objects.equals(text, name.text);
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }

    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return v.visitName(this, t);
    }

    @Override
    public Name copy() {
        return new Name(this.text);
    }
}
