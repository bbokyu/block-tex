lexer grammar BlockTexLexer;

// Keywords that start statements
CREATE       : 'Create' WS* -> mode(BLOCK_MODE);
SET          : 'Set' WS* -> mode(IDENTIFIER_MODE);
LOOP_START   : 'Loop' WS* -> mode(IDENTIFIER_MODE);
RENDER_START : 'Render' WS* -> mode(IDENTIFIER_MODE);

// Misc.
THIS         : 'This';
ASSIGN       : '<-' WS* -> mode(ASSIGN_MODE);
DOT          : '.' -> mode(PROPERTY_MODE);
COMMA        : ',';
LOOP_END     : 'End';
MULTIPLY     : WS* '*' WS* -> mode(ASSIGN_MODE);
SUBTRACT     : WS* '-' WS* -> mode(ASSIGN_MODE);
DIVIDE       : WS* '/' WS* -> mode(ASSIGN_MODE);
ADD          : WS* '+' WS* -> mode(ASSIGN_MODE);
WS           : [ \r\n\t]+ -> channel(HIDDEN); // Consolidated whitespace handling;

// Conditional Tokens
IF : 'if' WS*;
ELSE : 'else';
EQUALS  : '==';
G_T :'>';
L_T :'<';
GEQ : '>=';
LEQ : '<=';
OPEN_PAREN : '(';
CLOSE_PAREN : ')';
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
ROW : 'Row';
COL : 'Col';
CONST : '-'?[0-9]+;

mode ASSIGN_MODE;
A_NUM           : '-'?[0-9]+ -> mode(DEFAULT_MODE);
A_IDENTIFIER  : [a-zA-Z_][a-zA-Z_]* -> mode(DEFAULT_MODE);
A_ROW           : 'Row' -> mode(DEFAULT_MODE);
A_COL           : 'Col' -> mode(DEFAULT_MODE);
COORDINATE_START : '(' -> mode(COORDINATE_MODE);
RAW_LATEX : '"' ( '\\' '"' | ~["\\] )* '"' -> mode(DEFAULT_MODE);

mode BLOCK_MODE;
LATEX         : 'Latex' WS* -> mode(IDENTIFIER_MODE);
FRACTION      : 'Fraction' WS* -> mode(IDENTIFIER_MODE);
INTEGRAL      : 'Integral' WS* -> mode(IDENTIFIER_MODE);
SUMMATION     : 'Summation' WS* -> mode(IDENTIFIER_MODE);
DERIVATIVE    : 'Derivative' WS* -> mode(IDENTIFIER_MODE);
MATRIX        : 'Matrix' WS* -> mode(IDENTIFIER_MODE);

mode PROPERTY_MODE;
NUMERATOR     : 'numerator' -> mode(DEFAULT_MODE);
DENOMINATOR   : 'denominator' -> mode(DEFAULT_MODE);
START         : 'start' -> mode(DEFAULT_MODE);
END           : 'end' -> mode(DEFAULT_MODE);
EQUATION      : 'equation' -> mode(DEFAULT_MODE);
METHOD        : 'method' -> mode(DEFAULT_MODE);
TYPE          : 'type' -> mode(DEFAULT_MODE);
ORDER         : 'order' -> mode(DEFAULT_MODE);
TARGETVAR     : 'targetVar' -> mode(DEFAULT_MODE);
MATRIX_SHAPE  : 'shape' -> mode(DEFAULT_MODE);
MATRIX_ELEMENT: 'element' -> mode(DEFAULT_MODE);

mode IDENTIFIER_MODE;
IDENTIFIER    : [a-zA-Z_][a-zA-Z_]* -> mode(DEFAULT_MODE);

mode COORDINATE_MODE;
COORDINATE_END: WS* ')' -> mode(DEFAULT_MODE);
COORD_NUM     : [0-9]+ -> mode(COORDINATE_MODE);
SEP           : WS* COMMA WS* -> mode(COORDINATE_MODE);
