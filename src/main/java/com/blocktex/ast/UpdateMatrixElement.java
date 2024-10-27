package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

public class UpdateMatrixElement extends Statement{

    private final Name matrixName;
    private final Coordinate coordinate;
    private final Expression expression;

    public UpdateMatrixElement(Name matrixName, Coordinate co, Expression ex) {
        this.matrixName = matrixName;
        this.coordinate = co;
        this.expression = ex;
    }


    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Expression getExpression() {
        return expression;
    }

    public Name getMatrixName() {
        return matrixName;
    }

    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return v.visitUpdateMatrixElement(this, t);
    }
}
