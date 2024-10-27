package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

public class DerivativeObject extends EquationObject {
    private final Name name;
    private final Expression type;
    private final Expression order;
    private final Expression targetvar;

    public DerivativeObject(Name name, Expression type, Expression order, Expression targetvar) {
        this.name = name;
        this.type = type;
        this.order = order;
        this.targetvar = targetvar;
    }

    public Name getName() {
        return this.name;
    }

    public Expression getType() {
        return this.type;
    }

    public Expression getOrder() {
        return this.order;
    }

    public Expression getTargetvar() {
        return this.targetvar;
    }

    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return v.visitDerivativeObject(this, t);
    }
}
