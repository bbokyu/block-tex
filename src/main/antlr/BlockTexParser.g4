// Syntax Cleaned using ChatGPT
// Conditional Syntax from TinyVar
parser grammar BlockTexParser;
options { tokenVocab=BlockTexLexer; }

// The entry point that captures the overall structure of the input.
program : statement* render*  WS* EOF;

statement : (equationObject | loop | element_call);

// Final Objects to Render
render : RENDER_START name;

// Defines the main objects used in equations.
equationObject :  fractionObject
                | summationObject
                | derivativeObject
                | integralObject
                | matrixObject
                | latexObject;

block_statement : (equationObject | set_loop_element | conditional);
block : OPEN_BRACE  block_statement* CLOSE_BRACE;
condition : (EQUALS | G_T | L_T | GEQ | LEQ);

conditional : IF OPEN_PAREN expression condition expression CLOSE_PAREN block ELSE block;

// Loop structure within a matrix definition.
loop : LOOP_START name block LOOP_END;

// Raw latex object
latexObject : CREATE LATEX name ASSIGN RAW_LATEX;

// Matrix object definition with potential loops and element assignments.
matrixObject : matrixName shape_call;
matrixName : CREATE MATRIX name;
shape_call : set_property MATRIX_SHAPE ASSIGN coordinate;

// Update Matrix Element
element_call : set_property MATRIX_ELEMENT ASSIGN coordinate ASSIGN expression;

// Coordinate definition used in matrix objects.
coordinate : COORDINATE_START COORD_NUM SEP COORD_NUM COORDINATE_END;

// Fraction object with numerator and denominator calls.
fractionObject : fractionName numerator_call denominator_call;
fractionName : CREATE FRACTION name;
numerator_call: set_property NUMERATOR ASSIGN expression;
denominator_call: set_property DENOMINATOR ASSIGN expression;

// Derivative object with type, order, and target variable.
derivativeObject : derivativeName type_call order_call targetVar_call;
derivativeName: CREATE DERIVATIVE name;
type_call: set_property TYPE ASSIGN expression;
order_call: set_property ORDER ASSIGN expression;
targetVar_call: set_property TARGETVAR ASSIGN expression;

// Summation and integral objects with start, end, and equation calls.
summationObject : summationName start_call end_call equation_call;
summationName: CREATE SUMMATION name;
integralObject : integralName start_call end_call equation_call;
integralName: CREATE INTEGRAL name;

// Shared property setting calls for summation and integral.
start_call: set_property START ASSIGN expression;
end_call: set_property END ASSIGN expression;
equation_call: set_property EQUATION ASSIGN expression;

// Basic structure for setting properties of various objects.
set_property: SET IDENTIFIER DOT;
set_loop_element: THIS ASSIGN expression;

// Unified rule for capturing identifiers and numbers.
name : IDENTIFIER | A_IDENTIFIER;
number : A_NUM | CONST;

// Define Expression
expression : term (operation term)?;

term : (name | number | rowCol);
rowCol : (ROW | COL);
operation : (ADD | SUBTRACT | MULTIPLY | DIVIDE);
