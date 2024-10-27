package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

public class Coordinate extends Node{

    private final int row;
    private final int col;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getRowString() {
        return String.valueOf(row);
    }

    public String getColString() {
        return String.valueOf(col);
    }

    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return v.visitCoordinate(this, t);
    }
}
