package com.blocktex.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.blocktex.ast.RowCol;
import com.blocktex.ast.SetLoopElement;
import com.blocktex.ast.Statement;
import com.blocktex.ast.SummationObject;
import com.blocktex.ast.UpdateMatrixElement;

public class Evaluator implements BlockTexVisitor<StringBuilder, String> {

    private final Map<Name, EquationObject> objectMemory;

    private final Map<Name, String> latexMemory;

    private int row;
    private int col;


    private final List<String> result;

    public Evaluator() {
        this.objectMemory = new HashMap<>();
        this.latexMemory = new HashMap<>();
        this.result = new ArrayList<>();
    }

    public Map<Name, EquationObject> getMemory() {
        return objectMemory;
    }

    public List<String> getResult() {
        return result;
    }

    @Override
    public String visitProgram(Program p, StringBuilder errorLog) {

        for (Statement s : p.getStatementList()) {
            s.accept(this, errorLog);
        }

        // Create result based on renderObject list
        for (Name n : p.getRenderObjects()) {
            this.result.add(latexMemory.get(n));
        }

        return null;
    }


    public String visitStatement(Statement s, StringBuilder errorLog) {
        System.out.println("Inside Statement. This should never run.");
        s.accept(this, errorLog);
        return null;
    }


    // Credit: ChatGPT
    @Override
    public String visitCondition(Condition c, StringBuilder errorLog, int i, int j, MatrixObject mo) {

        try {
            // Evaluate both sides of the condition
            float leftValue = Float.parseFloat(convertRowColToNumber(c.getLeft()).accept(this, errorLog));
            float rightValue = Float.parseFloat(convertRowColToNumber(c.getRight()).accept(this, errorLog));

            boolean result;

            // Determine the result based on the condition
            switch (c.getCondition()) {
                case "==":
                    result = leftValue == rightValue;
                    break;
                case "!=":
                    result = leftValue != rightValue;
                    break;
                case "<":
                    result = leftValue < rightValue;
                    break;
                case ">":
                    result = leftValue > rightValue;
                    break;
                case "<=":
                    result = leftValue <= rightValue;
                    break;
                case ">=":
                    result = leftValue >= rightValue;
                    break;
                default:
                    errorLog.append("Unsupported condition: ").append(c.getCondition()).append("\n");
                    return "false"; // or handle the error differently
            }

            // Return the result as a string
            return String.valueOf(result);
        } catch (Exception e) {
            errorLog.append("Error evaluating condition: ").append(e.getMessage()).append("\n");
            return "false"; // In case of error, return "false" or handle it as needed
        }
    }

    @Override
    public String visitConditional(Conditional cl, StringBuilder errorLog, int i, int j, MatrixObject matrix) {
        Condition cond = cl.getCondition();

        if (cond.accept(this, errorLog, i, j, matrix).equals("true")) {
            cl.getIfBlock().accept(this, errorLog, i, j, matrix);
        } else {
            cl.getElseBlock().accept(this, errorLog, i, j, matrix);
        }

        return null;
    }


    // TODO: I dont think we need this. Remove later.
    @Override
    public String visitEquationObject(EquationObject eo, StringBuilder stringBuilder) {
        System.out.println("this shouldnt run");
        return null;
    }


    @Override
    public String visitLatexObject(LatexObject lo, StringBuilder errorLog) {
        Name name = lo.getName();
        String rawLatex = lo.getText();

        latexMemory.put(name, rawLatex);
        return null;
    }

    @Override
    public String visitFractionObject(FractionObject fo, StringBuilder errorLog) {
        Name name = fo.getName();

        // Create and store latex form
        String numerator = fo.getNumerator().accept(this, errorLog);
        String denominator = fo.getDenominator().accept(this, errorLog);

        String latexFormat = "\\frac" + "{" + numerator  + "}" + "{" + denominator + "}";

        latexMemory.put(name, latexFormat);

        return null;
    }

    @Override
    public String visitSummationObject(SummationObject so, StringBuilder errorLog) {
        Name name = so.getName();

        // Create and store latex form
        String start = so.getStart().accept(this, errorLog);
        String end = so.getEnd().accept(this, errorLog);
        String equation = so.getEquation().accept(this, errorLog);
        // System.out.println("equation:" + equation);

        // if accepting a number, use {end}, else {\\infty} by default
        String latexFormat = "\\sum_{i=" + start + "}^{\\infty} " + equation;
        if (end != null) {
            latexFormat = "\\sum_{i=" + start + "}^{" + end + "} " + equation;
        }
        // System.out.println("latexFormat:" + latexFormat);

        latexMemory.put(name, latexFormat);

        return null;
    }


    @Override
    public String visitLoop(Loop loop, StringBuilder errorLog) {
        Name matrixName = loop.getName();
        MatrixObject matrix = (MatrixObject) objectMemory.get(matrixName);

        int row_size = matrix.getRow_size();
        int col_size = matrix.getCol_size();

        Block block = loop.getBlock();

        // Start Loop
        for (int i = 0; i < row_size; i++) {
            for (int j = 0; j < col_size; j++) {
                // update global row and col latex
                this.row = i;
                this.col = j;
                block.accept(this, errorLog, i, j, matrix);
            }
        }

        // Update the Latex version of the Matrix
        updateMatrixLatex(matrix, errorLog);

        return null;
    }

    @Override
    public String visitBlock(Block block, StringBuilder errorLog, int i, int j, MatrixObject matrix) {
        for (BlockStatement block_statement : block.getBlock_statements()) {
            block_statement.accept(this, errorLog, i, j ,matrix);
        }

        return null;
    }

    public String visitSetLoopElement(SetLoopElement sle, StringBuilder errorLog, int i, int j, MatrixObject matrix) {
        Coordinate co = new Coordinate(i, j);

        // Create copies of expression. Very important. Otherwise, matrix ends up with all elements referencing to same expression
        Expression ex = sle.getExpression();

        Expression convertedExpression =  convertRowColToNumber(ex);

        matrix.updateElement(co, convertedExpression);
        return null;
    }



    // Credit: Simplified using ChatGPT
    // Converts "Row" or "Col" to the corresponding Number object, or returns the original expression
    private Expression convertToNumberIfNeeded(Expression term) {
        if (term instanceof Name) {
            String text = ((Name) term).getText();
            return switch (text) {
                case "Row" -> new Number(this.row);
                case "Col" -> new Number(this.col);
                default -> term;
            };
        } else if (term instanceof RowCol) {
            String text = ((RowCol) term).getText();
            return switch (text) {
                case "Row" -> new Number(this.row);
                case "Col" -> new Number(this.col);
                default -> term;
            };
        }
        return term;
    }

    public Expression convertRowColToNumber(Expression ex) {
        if (ex instanceof ArithmeticExpression) {
            ArithmeticExpression ae = (ArithmeticExpression) ex;
            // Use the helper function to convert both terms
            Expression newLeftTerm = convertToNumberIfNeeded(ae.getLeft());
            Expression newRightTerm = convertToNumberIfNeeded(ae.getRight());
            // Return a new ArithmeticExpression with the converted terms
            return new ArithmeticExpression(newLeftTerm, newRightTerm, ae.getOperator());
        } else {
            // Directly convert Name and RowCol instances or return the original expression
            return convertToNumberIfNeeded(ex);
        }
    }




    @Override
    public String visitArithmeticExpression(ArithmeticExpression ae, StringBuilder errorLog) {
        Expression leftTerm = ae.getLeft();
        Expression rightTerm  = ae.getRight();
        String operation = ae.getOperator();

        // Combine if both sides are numbers
        if (leftTerm.getClass() == Number.class && rightTerm.getClass() == Number.class) {
            return String.format("%.1f", calculate(((Number) leftTerm).getValue(), ((Number) rightTerm).getValue(), operation));
        } else {
            // Leave as is
            return leftTerm.accept(this, errorLog) + operation + rightTerm.accept(this, errorLog);
        }

    }

    @Override
    public String visitDerivativeObject(DerivativeObject deo, StringBuilder sb) {
        Name name = deo.getName();
        // System.out.println("Derivative name: " + name);

        // Create and store latex form
        String type = deo.getType().accept(this, sb);
        // System.out.println("Derivative type: " + type);
        String order = deo.getOrder().accept(this, sb);
        // System.out.println("Derivative order: " + order);
        String targetVariable = deo.getTargetvar().accept(this, sb);
        if (targetVariable == null) {
            targetVariable = "x";
        }
        // System.out.println("Target Variable: " + targetVariable);

        String latex;

        if (Objects.equals(type, "partial")) {
            latex = "\\frac" + "{d" + targetVariable + "}" + "{dx}";
        } else {
            latex = "\\frac" + "{d}" + "{d" + targetVariable + "}";
        }
        System.out.println("Latex : " + latex);

        latexMemory.put(name, latex);

        return null;
    }

    @Override
    public String visitIntegralObject(IntegralObject io, StringBuilder sb) {
        Name name = io.getName();

        String equation = io.getEquation().accept(this, sb);
        if (equation == null) { equation = "f(t)"; }
        System.out.println("Integrand: " + equation);
        String start = io.getStart().accept(this, sb);
        if (start == null) { start = "a"; }
        System.out.println("Integral start: " + start);
        String end = io.getEnd().accept(this, sb);
        if (end == null) { end = "b"; }
        System.out.println("Integral end: " + end);

        // Example input: \int_{a}^{b} x^2 \,dx
        // \int_{1}^{2} x^2 \, dx <- previous output
        // \\int_{a}^{x}f(t)dt <- actual react-latex format
        String latex = "\\int_{" + start +"}^{" + end + "}" + equation + "dx";
        // String latex = "\\int_{" + start +"}^{" + end + "} " + equation + " \\, dx";
        System.out.println("Latex: " + latex);

        latexMemory.put(name, latex);

        return null;
    }

    @Override
    public String visitUpdateMatrixElement(UpdateMatrixElement um, StringBuilder errorLog) {
        Name nameToUpdate = um.getMatrixName();
        Coordinate co = um.getCoordinate();
        Expression ex = um.getExpression();

        MatrixObject matrix = (MatrixObject) objectMemory.get(nameToUpdate);

        // update the matrix in object memory
        if (!matrix.updateElement(co, ex)) {
            System.out.println("Invalid Coordinate to Update Matrix " + nameToUpdate.getText());
            return null;
        }

        // update the latex version as well
        String latexMatrix = toLaTeX(matrix, errorLog);
        latexMemory.put(matrix.getName(), latexMatrix);

        return null;
    }



    @Override
    public String visitMatrixObject(MatrixObject mo, StringBuilder errorLog) {

        // Add Matrix to object memory
        objectMemory.put(mo.getName(), mo);

        // Add Latex form to latex memory
        updateMatrixLatex(mo, errorLog);

        return null;
    }


    public Void updateMatrixLatex(MatrixObject mo, StringBuilder errorLog) {
        String latexMatrix = toLaTeX(mo, errorLog);
        latexMemory.put(mo.getName(), latexMatrix);
        return null;
    }


    // Credit: General logic by ChatGPT
    public String toLaTeX(MatrixObject mo, StringBuilder errorLog) {
        int row_size = mo.getRow_size();
        int col_size = mo.getCol_size();
        Expression[][] matrix = mo.getMatrix();

        StringBuilder latex = new StringBuilder();
        latex.append("\\begin{bmatrix}\n");
        for (int i = 0; i < row_size; i++) {
            for (int j = 0; j < col_size; j++) {

                String element = matrix[i][j].accept(this, errorLog);
                latex.append(element);

                if (j < col_size - 1) {
                    latex.append(" & "); // LaTeX column separator
                }
            }
            latex.append(" \\\\\n"); // LaTeX row ending and new line
        }
        latex.append("\\end{bmatrix}");
        return latex.toString();
    }

    // Credit: General Structure by ChatGPT
    public float calculate(float leftTerm, float rightTerm, String operation) {
        String trimmedOperation = operation.trim();
        return switch (trimmedOperation) {
            case "+" -> leftTerm + rightTerm;
            case "-" -> leftTerm - rightTerm;
            case "*" -> leftTerm * rightTerm;
            case "/" -> {
                if (rightTerm == 0) {
                    System.out.println("Please do not divide by 0");
                }
                yield leftTerm / rightTerm;
            }
            default -> {
                System.out.println("Illegal Operation");
                yield 0;
            }
        };
    }


    @Override
    public String visitName(Name na, StringBuilder errorLog) {
        return latexMemory.get(na);
    }

    @Override
    public String visitNumber(Number nu, StringBuilder errorLog) {
        return nu.toString();
    }

    @Override
    public String visitCoordinate(Coordinate co, StringBuilder stringBuilder) {
        return null;
    }


    @Override
    public String visitExpression(Expression ex, StringBuilder errorLog) {
        System.out.println("THIS SHOULDN'T RUN");
        return null;
    }


    // TODO Remove this later
    @Override
    public String visitBlockStatement(BlockStatement bs, StringBuilder stringBuilder, int row, int col, MatrixObject mo) {
        System.out.println("this should never run");
        return null;
    }
}
