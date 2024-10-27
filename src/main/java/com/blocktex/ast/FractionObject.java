package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

public class FractionObject extends EquationObject {
    private final Name name;
    private final Expression numerator;
    private final Expression denominator;

    public FractionObject(Name name, Expression numerator, Expression denominator) {
        this.name = name;
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Name getName() {
        return this.name;
    }

    public Expression getNumerator() {
        return this.numerator;
    }

    public Expression getDenominator() {
        return this.denominator;
    }

    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return v.visitFractionObject(this, t);
    }
}
