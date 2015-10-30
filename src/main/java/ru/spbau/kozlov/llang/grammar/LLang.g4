grammar LLang;

@header {
import ru.spbau.kozlov.llang.ast.*;
import ru.spbau.kozlov.llang.ast.expressions.*;
import ru.spbau.kozlov.llang.ast.statements.*;
import ru.spbau.kozlov.llang.ast.types.*;
}

program returns [ProgramNode node]
    :   delimiter topLevelDeclarationStatement ( delimiter topLevelDeclarationStatement )* delimiter
    ;

topLevelDeclarationStatement returns [TopLevelDeclarationStatementNode node]
    :   functionDeclarationStatement
    |   caseClassDeclarationStatement
    ;

functionDeclarationStatement returns [FunctionDeclarationStatementNode node]
    :   DEFINITION WS+ identifier WS* typedIdentifierList WS* COLON WS* type WS* ASSIGN WS* blockExpression
    ;

caseClassDeclarationStatement returns [CaseClassDeclarationStatementNode node]
    : CASE WS+ CLASS WS+ identifier WS* typedIdentifierList
    ;

statement returns [StatementNode node]
    :   topLevelDeclarationStatement
    |   expression
    ;

expression returns [ExpressionNode node]
    :   primaryExpression
    |   variableDeclarationExpression
    |   blockExpression
    |   ifExpression
    |   whileExpression
    |   returnExpression
    |   NEW WS+ expression SPACE* expressionList
    |   expression SPACE* expressionList
    |   expression WS* innerAccess
    |   ( ADD_OP | SUB_OP ) WS* expression
    |   NOT_OP WS* expression
    |   expression WS* ( MUL_OP | DIV_OP | MOD_OP ) WS* expression
    |   expression WS* ( ADD_OP | SUB_OP ) WS* expression
    |   expression WS* ( LE | LEQ | GT | GEQ ) WS* expression
    |   expression WS* ( EQ | NEQ ) WS* expression
    |   expression WS* AND_OP WS* expression
    |   expression WS* XOR_OP WS* expression
    |   expression WS* OR_OP WS* expression
    |   <assoc=right> expression WS* ASSIGN WS* expression
    ;

variableDeclarationExpression returns [VariableDeclarationExpressionNode node]
    :   ( VAL | VAR ) WS+ typedIdentifier WS* ASSIGN WS* expression
    ;

blockExpression returns [BlockExpressionNode node]
    : LEFT_BRACE delimiter ( delimiter statement )* delimiter RIGHT_BRACE
    ;

ifExpression returns [IfExpressionNode node]
    :   IF WS* LEFT_PAREN WS* expression WS* RIGHT_PAREN WS* blockExpression ( WS* ELSE WS* blockExpression )?
    ;

whileExpression returns [WhileExpressionNode node]
    :   WHILE WS* LEFT_PAREN WS* expression WS* RIGHT_PAREN WS* blockExpression
    ;

returnExpression returns [ReturnExpressionNode node]
    :   RETURN ( WS+ expression )?
    ;

innerAccess returns [InnerAccessNode node]
    :   DOT WS* ( identifier | ( UNDERSCORE integerLiteral ) )
    ;

expressionList returns [ExpressionListNode node]
    :   LEFT_PAREN WS* ( expression WS* ( COMMA WS* expression WS* )* )? RIGHT_PAREN
    ;

primaryExpression returns [ExpressionNode node]
    :   LEFT_PAREN WS* expression WS* ( COMMA WS* expression WS* )* RIGHT_PAREN
    |   identifier
    |   booleanLiteral
    |   integerLiteral
    |   stringLiteral
    ;

booleanLiteral returns [boolean value]
    :   TRUE
    |   FALSE
    ;

integerLiteral returns [int value]
    :   ZERO
    |   ( NON_ZERO_DECIMAL_DIGIT ( NON_ZERO_DECIMAL_DIGIT | ZERO )* )
    ;

stringLiteral returns [String value]
    :   QUOTATION_MARK ( ~QUOTATION_MARK )* QUOTATION_MARK
    ;

typedIdentifier returns [TypedIdentifierExpressionNode node]
    :   identifier WS* COLON WS* type
    ;

typedIdentifierList returns [TypedIdentifierListNode node]
    :   LEFT_PAREN WS* ( typedIdentifier WS* ( COMMA WS* typedIdentifier WS* )* )? RIGHT_PAREN
    ;

identifier returns [String name]
    :   id
    |   GRAVE_ACCENT id GRAVE_ACCENT
    ;

id
    :   (
            LOWER_CASE
        |   UPPER_CASE
        ) idSuffix?
    ;

idSuffix
    :   (
        LOWER_CASE
    |   UPPER_CASE
    |   DECIMAL_DIGIT
    |   UNDERSCORE
    )+
    ;

type returns [TypeNode node]
    :   builtInType
    |   clazz
    |   tuple
    ;

builtInType returns [BuiltInTypeNode node]
    :   ANY
    |   ANY_VAL
    |   INT
    |   BOOLEAN
    |   STRING
    |   UNIT
    ;

clazz returns [ClassNode node]
    :   identifier
    ;

tuple returns [TupleNode node]
    :   LEFT_PAREN WS* type WS* ( COMMA WS* type WS* )* RIGHT_PAREN
    ;

delimiter
    :   WS* ( ( SEMICOLON+ WS* ) | WS+ )*
    ;

WS : [ \t\r\n] ;
SPACE : [ \t] ;

LINE_COMMENT : LINE_COMMENT_PREFIX ~[\r\n]*? ( [\r\n]+ | EOF ) -> skip ;
MULTILINE_COMMENT : MULTILINE_COMMENT_PREFIX .*? MULTILINE_COMMENT_SUFFIX -> skip ;

SEMICOLON : ';' ;
COLON : ':' ;
COMMA : ',' ;
DOT : '.' ;
GRAVE_ACCENT : '`' ;
QUOTATION_MARK : '"' ;
LOWER_CASE : 'a' .. 'z' ;
UPPER_CASE : 'A' .. 'Z' ;
ZERO : '0' ;
NON_ZERO_DECIMAL_DIGIT : '1' .. '9' ;
DECIMAL_DIGIT : '0' .. '9' ;
UNDERSCORE : '_' ;
ASSIGN : '=' ;

LINE_COMMENT_PREFIX : '//' ;
MULTILINE_COMMENT_PREFIX : '/*' ;
MULTILINE_COMMENT_SUFFIX : '*/' ;

LEFT_PAREN : '(' ;
RIGHT_PAREN : ')' ;
LEFT_BRACE : '{' ;
RIGHT_BRACE : '}' ;

// operators
ADD_OP : '+' ;
SUB_OP : '-' ;
MUL_OP : '*' ;
DIV_OP : '/' ;
MOD_OP : '%' ;

OR_OP : '||' ;
XOR_OP : '^' ;
AND_OP : '&&' ;
NOT_OP : '!' ;
TRUE : 'true' ;
FALSE : 'false' ;

EQ : '==' ;
NEQ : '!=' ;
LE : '<' ;
LEQ : '<=' ;
GT : '>' ;
GEQ : '>=' ;

// types
ANY : 'Any' ;
ANY_VAL : 'AnyVal' ;
INT : 'Int' ;
BOOLEAN : 'Boolean' ;
STRING : 'String' ;
UNIT : 'Unit' ;

// keywords
DEFINITION : 'def' ;
UNDEFINED : '???' ;
CASE : 'case' ;
CLASS : 'class' ;
IF : 'if' ;
ELSE : 'else' ;
WHILE : 'while' ;
RETURN : 'return' ;
NEW : 'new' ;
VAL : 'val' ;
VAR : 'var' ;