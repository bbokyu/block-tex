package com.blocktex.ast;

import java.util.List;

import com.blocktex.visitor.BlockTexVisitor;

public class Block extends Node {

    private final List<BlockStatement> block_statements;

    public Block(List<BlockStatement> block_statements) {
        this.block_statements = block_statements;
    }

    public List<BlockStatement> getBlock_statements() {
        return block_statements;
    }

    public <T, U> U accept(BlockTexVisitor<T, U> v, T t, int i, int j, MatrixObject matrix) {
        return v.visitBlock(this, t, i , j, matrix);
    }

    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return null;
    }
}
