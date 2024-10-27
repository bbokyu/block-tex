package com.blocktex.ast;

import com.blocktex.visitor.BlockTexVisitor;

public class MatrixObject extends EquationObject {
    private final Name name;

    private final int row_size;
    private  final int col_size;

    private final Expression[][] matrix;

    public MatrixObject(Name name, int row_size, int col_size) {
        this.name = name;
        this.row_size = row_size;
        this.col_size = col_size;
        this.matrix = new Expression[row_size][col_size];
        initializeMatrix();
    }

    private void initializeMatrix() {
        for (int i = 0; i < row_size; i++) {
            for (int j = 0; j < col_size; j++) {
                this.matrix[i][j] = new Number(0);
            }
        }
    }

    public Boolean updateElement(Coordinate co, Expression ex) {
        int row = co.getRow();
        int col = co.getCol();

        if (row < 0 || row > row_size - 1 || col < 0 || col > col_size - 1) {
            return false;
        }

        this.matrix[row][col] = ex;

        return true;
    }


    public void printMatrixState() {
        System.out.println("Current Matrix State:");
        for (int i = 0; i < row_size; i++) {
            for (int j = 0; j < col_size; j++) {
                System.out.print(System.identityHashCode(this.matrix[i][j]) + " ");
            }
            System.out.println(); // Move to the next line after printing a row
        }
    }

    public Name getName() {
        return this.name;
    }

    public int getRow_size() {
        return row_size;
    }

    public int getCol_size() {
        return col_size;
    }

    public Expression[][] getMatrix() {
//        System.out.println(matrix);
        return matrix;
    }

    @Override
    public <T, U> U accept(BlockTexVisitor<T, U> v, T t) {
        return v.visitMatrixObject(this, t);
    }
}
