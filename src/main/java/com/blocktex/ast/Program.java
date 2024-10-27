package com.blocktex.ast;

import java.util.List;

import com.blocktex.visitor.BlockTexVisitor;

public class Program extends Node {
    // private final List<EquationObject> equationObjects;
    private final List<Statement> statementList;
    private final List<Name> renderObjects;

    public Program(List<Statement> statementList, List<Name> renderObjects) {
        // this.equationObjects = equationObjects;
        this.statementList = statementList;
        this.renderObjects = renderObjects;
    }

    public List<Statement> getStatementList() {
        return statementList;
    }

    public List<Name> getRenderObjects() {
        return renderObjects;
    }

    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return v.visitProgram(this, t);
    }

}