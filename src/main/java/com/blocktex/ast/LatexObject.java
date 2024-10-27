package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

public class LatexObject extends EquationObject {

    private final Name name;
    private final String text;

    public LatexObject(Name name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getText() {
        return text;
    }


    public Name getName() {
        return name;
    }

    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return v.visitLatexObject(this, t);
    }

}
