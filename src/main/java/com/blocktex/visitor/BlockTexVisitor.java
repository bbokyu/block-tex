package com.blocktex.visitor;

import com.blocktex.ast.ArithmeticExpression;
import com.blocktex.ast.Block;
import com.blocktex.ast.BlockStatement;
import com.blocktex.ast.Condition;
import com.blocktex.ast.Conditional;
import com.blocktex.ast.Coordinate;
import com.blocktex.ast.DerivativeObject;
import com.blocktex.ast.EquationObject;
import com.blocktex.ast.Expression;
import com.blocktex.ast.FractionObject;
import com.blocktex.ast.IntegralObject;
import com.blocktex.ast.LatexObject;
import com.blocktex.ast.Loop;
import com.blocktex.ast.MatrixObject;
import com.blocktex.ast.Name;
import com.blocktex.ast.Number;
import com.blocktex.ast.Program;
import com.blocktex.ast.SetLoopElement;
import com.blocktex.ast.Statement;
import com.blocktex.ast.SummationObject;
import com.blocktex.ast.UpdateMatrixElement;

public interface BlockTexVisitor<T,U> {

    // Core AST node visits
    U visitProgram(Program p, T t);
    U visitStatement(Statement s, T t);

    // Mathematical objects
    U visitEquationObject(EquationObject eo, T t);
    U visitFractionObject(FractionObject fo, T t);
    U visitSummationObject(SummationObject so, T t);
    U visitDerivativeObject(DerivativeObject deo, T t);
    U visitIntegralObject(IntegralObject io, T t);
    U visitMatrixObject(MatrixObject mo, T t);
    U visitLatexObject(LatexObject lo, T t);

    // Basic elements
    U visitName(Name na, T t);
    U visitNumber(Number nu, T t);

    // Expressions
    U visitExpression(Expression ex, T t);
    U visitArithmeticExpression(ArithmeticExpression ae, T t);
    U visitCoordinate(Coordinate co, T t);

    // Matrix operations
    U visitUpdateMatrixElement(UpdateMatrixElement um, T t);

    // Block-related visits
    U visitBlock(Block block, T t, int row, int col, MatrixObject matrix);
    U visitBlockStatement(BlockStatement bs, T t, int row, int col, MatrixObject mo);

    // Control flow
    U visitLoop(Loop loop, T t);
    U visitSetLoopElement(SetLoopElement sle, T t, int i, int j, MatrixObject mo);
    U visitCondition(Condition c, T t, int i, int j, MatrixObject mo);
    U visitConditional(Conditional cl, T t, int i, int j, MatrixObject mo);


}
