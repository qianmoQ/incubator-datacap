grammar SqlBase;

singleStatement: (statement)* EOF;

// Keywords
SELECT: [Ss][Ee][Ll][Ee][Cc][Tt];
FROM: [Ff][Rr][Oo][Mm];
WHERE: [Ww][Hh][Ee][Rr][Ee];
ORDER: [Oo][Rr][Dd][Ee][Rr];
BY: [Bb][Yy];
GROUP: [Gg][Rr][Oo][Uu][Pp];
HAVING: [Hh][Aa][Vv][Ii][Nn][Gg];
LIMIT: [Ll][Ii][Mm][Ii][Tt];
OFFSET: [Oo][Ff][Ff][Ss][Ee][Tt];
ASC: [Aa][Ss][Cc];
DESC: [Dd][Ee][Ss][Cc];
AS: [Aa][Ss];
ON: [Oo][Nn];
USING: [Uu][Ss][Ii][Nn][Gg];
INNER: [Ii][Nn][Nn][Ee][Rr];
LEFT: [Ll][Ee][Ff][Tt];
RIGHT: [Rr][Ii][Gg][Hh][Tt];
FULL: [Ff][Uu][Ll][Ll];
OUTER: [Oo][Uu][Tt][Ee][Rr];
JOIN: [Jj][Oo][Ii][Nn];
CROSS: [Cc][Rr][Oo][Ss][Ss];
NATURAL: [Nn][Aa][Tt][Uu][Rr][Aa][Ll];
INSERT: [Ii][Nn][Ss][Ee][Rr][Tt];
INTO: [Ii][Nn][Tt][Oo];
VALUES: [Vv][Aa][Ll][Uu][Ee][Ss];
UPDATE: [Uu][Pp][Dd][Aa][Tt][Ee];
SET: [Ss][Ee][Tt];
DELETE: [Dd][Ee][Ll][Ee][Tt][Ee];
CREATE: [Cc][Rr][Ee][Aa][Tt][Ee];
ALTER: [Aa][Ll][Tt][Ee][Rr];
DROP: [Dd][Rr][Oo][Pp];
TABLE: [Tt][Aa][Bb][Ll][Ee];
VIEW: [Vv][Ii][Ee][Ww];
INDEX: [Ii][Nn][Dd][Ee][Xx];
PRIMARY: [Pp][Rr][Ii][Mm][Aa][Rr][Yy];
KEY: [Kk][Ee][Yy];
FOREIGN: [Ff][Oo][Rr][Ee][Ii][Gg][Nn];
REFERENCES: [Rr][Ee][Ff][Ee][Rr][Ee][Nn][Cc][Ee][Ss];
CONSTRAINT: [Cc][Oo][Nn][Ss][Tt][Rr][Aa][Ii][Nn][Tt];
DEFAULT: [Dd][Ee][Ff][Aa][Uu][Ll][Tt];
UNIQUE: [Uu][Nn][Ii][Qq][Uu][Ee];
CHECK: [Cc][Hh][Ee][Cc][Kk];
COLUMN: [Cc][Oo][Ll][Uu][Mm][Nn];
DATABASE: [Dd][Aa][Tt][Aa][Bb][Aa][Ss][Ee];
USE: [Uu][Ss][Ee];
IF: [Ii][Ff];
EXISTS: [Ee][Xx][Ii][Ss][Tt][Ss];
REPLACE: [Rr][Ee][Pp][Ll][Aa][Cc][Ee];
TEMP: [Tt][Ee][Mm][Pp];
TEMPORARY: [Tt][Ee][Mm][Pp][Oo][Rr][Aa][Rr][Yy];
SHOW: [Ss][Hh][Oo][Ww];
DATABASES: [Dd][Aa][Tt][Aa][Bb][Aa][Ss][Ee][Ss];
TABLES: [Tt][Aa][Bb][Ll][Ee][Ss];
COLUMNS: [Cc][Oo][Ll][Uu][Mm][Nn][Ss];
CREATE_TIME: [Cc][Rr][Ee][Aa][Tt][Ee]'_'[Tt][Ii][Mm][Ee];
UPDATE_TIME: [Uu][Pp][Dd][Aa][Tt][Ee]'_'[Tt][Ii][Mm][Ee];

// Operators
AND: [Aa][Nn][Dd];
OR: [Oo][Rr];
NOT: [Nn][Oo][Tt];
IS: [Ii][Ss];
NULL: [Nn][Uu][Ll][Ll];
LIKE: [Ll][Ii][Kk][Ee];
IN: [Ii][Nn];
BETWEEN: [Bb][Ee][Tt][Ww][Ee][Ee][Nn];
CASE: [Cc][Aa][Ss][Ee];
WHEN: [Ww][Hh][Ee][Nn];
THEN: [Tt][Hh][Ee][Nn];
ELSE: [Ee][Ll][Ss][Ee];
END: [Ee][Nn][Dd];

// Others
CHARSET: [Cc][Hh][Aa][Rr][Ss][Ee][Tt];
COLLATE: [Cc][Oo][Ll][Ll][Aa][Tt][Ee];
AUTO_INCREMENT: [Aa][Uu][Tt][Oo]'_'[Ii][Nn][Cc][Rr][Ee][Mm][Ee][Nn][Tt];
COMMENT: [Cc][Oo][Mm][Mm][Ee][Nn][Tt];
ADD: [Aa][Dd][Dd];
MODIFY: [Mm][Oo][Dd][Ii][Ff][Yy];
ENGINE: [Ee][Nn][Gg][Ii][Nn][Ee];
VERSION: [Vv][Ee][Rr][Ss][Ii][Oo][Nn];

tableOptions
    : tableOption+
    ;

tableOption
    : ENGINE '=' STRING
    | CHARSET '=' STRING
    | COLLATE '=' STRING
    | AUTO_INCREMENT '=' INTEGER_VALUE
    | COMMENT '=' STRING
    ;

statement
    : selectStatement
    | insertStatement
    | updateStatement
    | deleteStatement
    | createStatement
    | alterStatement
    | dropStatement
    | useStatement
    | showStatement
    ;

// USE statement
useStatement
    : USE databaseName
    ;

// SELECT statement
selectStatement
    : queryExpression
      orderByClause?
      limitClause?
    ;

queryExpression
    : queryTerm
    | queryExpression UNION (ALL)? queryTerm
    | queryExpression EXCEPT queryTerm
    | queryExpression INTERSECT queryTerm
    ;

queryTerm
    : queryPrimary
    ;

queryPrimary
    : querySpecification
    | '(' queryExpression ')'
    ;

querySpecification
    : SELECT (ALL | DISTINCT)? selectElements
      fromClause?
      whereClause?
      groupByClause?
      havingClause?
    ;

selectElements
    : selectElement (',' selectElement)*
    ;

selectElement
    : (tableName '.')? (columnName | '*') (AS? alias)?
    | expression (AS? alias)?
    | caseExpression (AS? alias)?
    ;

caseExpression
    : CASE
      (WHEN expression THEN expression)+
      (ELSE expression)?
      END
    ;

whereClause
    : WHERE expression
    ;

groupByClause
    : GROUP BY groupByElement (',' groupByElement)*
    ;

groupByElement
    : expression
    ;

havingClause
    : HAVING expression
    ;

orderByClause
    : ORDER BY orderByElement (',' orderByElement)*
    ;

orderByElement
    : expression (ASC | DESC)?
    ;

limitClause
    : LIMIT INTEGER_VALUE (OFFSET INTEGER_VALUE)?
    | LIMIT INTEGER_VALUE ',' INTEGER_VALUE
    ;

// INSERT statement
insertStatement
    : INSERT (OR REPLACE)? INTO tableName
      ('(' columnName (',' columnName)* ')')?
      (VALUES insertValuesConstructor (',' insertValuesConstructor)*
      | selectStatement)
    ;

insertValuesConstructor
    : '(' value (',' value)* ')'
    ;

// UPDATE statement
updateStatement
    : UPDATE tableName
      SET updateElement (',' updateElement)*
      whereClause?
    ;

updateElement
    : columnName '=' expression
    ;

// DELETE statement
deleteStatement
    : DELETE FROM tableName
      whereClause?
    ;

// CREATE statement
createStatement
    : createTableStatement
    | createViewStatement
    | createIndexStatement
    | createDatabaseStatement
    ;

createDatabaseStatement
    : CREATE DATABASE (IF NOT EXISTS)? databaseName
    ;

createTableStatement
    : CREATE (TEMP | TEMPORARY)? TABLE (IF NOT EXISTS)? tableName
      '(' tableElement (',' tableElement)* ')'
      tableOptions?
    ;

createViewStatement
    : CREATE (OR REPLACE)? VIEW tableName
      ('(' columnName (',' columnName)* ')')?
      AS selectStatement
    ;

createIndexStatement
    : CREATE (UNIQUE)? INDEX indexName
      ON tableName '(' indexColumn (',' indexColumn)* ')'
    ;

indexColumn
    : columnName (ASC | DESC)?
    ;

tableElement
    : columnDefinition
    | tableConstraint
    ;

columnDefinition
    : columnName dataType columnConstraint*
    ;

columnConstraint
    : NOT? NULL
    | PRIMARY KEY
    | UNIQUE
    | DEFAULT defaultValue
    | (CONSTRAINT constraintName)? foreignKeyClause
    | (CONSTRAINT constraintName)? checkConstraint
    ;

tableConstraint
    : (CONSTRAINT constraintName)?
      ( primaryKeyConstraint
      | uniqueConstraint
      | foreignKeyConstraint
      | checkConstraint
      )
    ;

primaryKeyConstraint
    : PRIMARY KEY '(' columnName (',' columnName)* ')'
    ;

uniqueConstraint
    : UNIQUE '(' columnName (',' columnName)* ')'
    ;

foreignKeyConstraint
    : FOREIGN KEY '(' columnName (',' columnName)* ')'
      foreignKeyClause
    ;

foreignKeyClause
    : REFERENCES tableName ('(' columnName (',' columnName)* ')')?
      (ON DELETE referenceOption)?
      (ON UPDATE referenceOption)?
    ;

referenceOption
    : RESTRICT
    | CASCADE
    | SET NULL
    | NO ACTION
    | SET DEFAULT
    ;

checkConstraint
    : CHECK '(' expression ')'
    ;

// ALTER statement
alterStatement
    : alterTableStatement
    ;

alterTableStatement
    : ALTER TABLE tableName
      alterSpecification (',' alterSpecification)*
    ;

alterSpecification
    : ADD COLUMN? columnDefinition
    | ADD tableConstraint
    | DROP COLUMN columnName
    | DROP CONSTRAINT constraintName
    | MODIFY COLUMN? columnDefinition
    | ALTER COLUMN columnName SET DEFAULT expression
    | ALTER COLUMN columnName DROP DEFAULT
    ;

// DROP statement
dropStatement
    : dropTableStatement
    | dropViewStatement
    | dropIndexStatement
    | dropDatabaseStatement
    ;

dropTableStatement
    : DROP TABLE (IF EXISTS)? tableName (',' tableName)*
    ;

dropViewStatement
    : DROP VIEW (IF EXISTS)? tableName (',' tableName)*
    ;

dropIndexStatement
    : DROP INDEX indexName ON tableName
    ;

dropDatabaseStatement
    : DROP DATABASE (IF EXISTS)? databaseName
    ;

// SHOW statement
showStatement
    : showDatabasesStatement
    | showTablesStatement
    | showColumnsStatement
    ;

showDatabasesStatement
    : SHOW DATABASES (LIKE STRING)?
    ;

showTablesStatement
    : SHOW TABLES
      (FROM | IN)? databaseName?
      (LIKE STRING | WHERE expression)?
    ;

showColumnsStatement
    : SHOW COLUMNS
      (FROM | IN) tableName
      ((FROM | IN) databaseName)?
      (LIKE STRING | WHERE expression)?
    ;

// FROM clause and JOINs
fromClause
    : FROM tableSource (',' tableSource)*
    ;

tableSource
    : tablePrimary
    | joinedTable
    ;

tablePrimary
    : tableName (AS? alias)?
    | '(' selectStatement ')' (AS? alias)?
    ;

joinedTable
    : tablePrimary joinClause+
    ;

joinType
    : INNER
    | LEFT OUTER?
    | RIGHT OUTER?
    | FULL OUTER?
    ;

joinClause
    : joinTypeClause tablePrimary joinCondition
    | NATURAL (INNER | LEFT | RIGHT | FULL)? JOIN tablePrimary
    | CROSS JOIN tablePrimary
    ;

joinTypeClause
    : INNER JOIN
    | LEFT OUTER? JOIN
    | RIGHT OUTER? JOIN
    | FULL OUTER? JOIN
    | JOIN
    ;

joinCondition
    : ON expression
    | USING '(' columnName (',' columnName)* ')'
    ;

// Expressions
expression
     : '(' expression ')'                                    #ParenExpression
     | primary                                              #PrimaryExpression
     | expression comparisonOperator expression             #ComparisonExpression
     | expression AND expression                            #AndExpression
     | expression OR expression                             #OrExpression
     | expression NOT? BETWEEN expression AND expression    #BetweenExpression
     | expression NOT? IN ('(' expression (',' expression)* ')')  #InExpression
     | expression NOT? LIKE expression                      #LikeExpression      // 添加这一行
     | expression IS NOT? NULL                              #IsNullExpression
     | expression IS NOT? (TRUE | FALSE)                    #IsBooleanExpression
     | NOT expression                                       #NotExpression
     | expression '+' expression                            #AddExpression
     | expression '-' expression                            #SubtractExpression
     | expression '*' expression                            #MultiplyExpression
     | expression '/' expression                            #DivideExpression
     ;

 primary
     : literal               #LiteralPrimary
     | columnReference      #ColumnReferencePrimary
     | functionCall        #FunctionCallPrimary
     ;

 literal
     : STRING
     | INTEGER_VALUE
     | DECIMAL_VALUE
     | TRUE
     | FALSE
     | NULL
     ;

 comparisonOperator
     : '=' | '>' | '<' | '>=' | '<=' | '<>' | '!=' | '<=>'
     ;

expressionList
    : '(' expression (',' expression)* ')'
    | selectStatement
    ;

columnReference
    : (tableName '.')? columnName
    ;

functionCall
    : functionName '(' (DISTINCT? expression (',' expression)*)? ')'
    | CAST '(' expression AS dataType ')'
    | EXTRACT '(' identifier FROM expression ')'
    | VERSION '(' ')'
    ;

// Common elements
value: expression;

defaultValue
    : literal
    | '(' expression ')'
    ;

// Names and Identifiers
columnName: identifier;
tableName: (databaseName '.')? identifier;
databaseName: identifier;
indexName: identifier;
constraintName: identifier;
alias: identifier;
functionName: identifier;

identifier
    : IDENTIFIER
    | quotedIdentifier
    | nonReservedWord
    ;

quotedIdentifier
    : BACKQUOTED_IDENTIFIER
    ;

// Data Types
dataType
    : baseDataType ('(' INTEGER_VALUE (',' INTEGER_VALUE)* ')')?
    ;

baseDataType
    : CHARACTER | VARCHAR | BINARY | VARBINARY
    | TINYINT | SMALLINT | INTEGER | INT | BIGINT
    | FLOAT | REAL | DOUBLE | DECIMAL | NUMERIC
    | DATE | TIME | TIMESTAMP | DATETIME
    | BOOLEAN | BOOL
    | BLOB | TEXT
    | JSON | XML
    ;

// Additional Keywords
UNION: [Uu][Nn][Ii][Oo][Nn];
ALL: [Aa][Ll][Ll];
DISTINCT: [Dd][Ii][Ss][Tt][Ii][Nn][Cc][Tt];
EXCEPT: [Ee][Xx][Cc][Ee][Pp][Tt];
INTERSECT: [Ii][Nn][Tt][Ee][Rr][Ss][Ee][Cc][Tt];
CHARACTER: [Cc][Hh][Aa][Rr][Aa][Cc][Tt][Ee][Rr];
VARCHAR: [Vv][Aa][Rr][Cc][Hh][Aa][Rr];
BINARY: [Bb][Ii][Nn][Aa][Rr][Yy];
VARBINARY: [Vv][Aa][Rr][Bb][Ii][Nn][Aa][Rr][Yy];
TINYINT: [Tt][Ii][Nn][Yy][Ii][Nn][Tt];
SMALLINT: [Ss][Mm][Aa][Ll][Ll][Ii][Nn][Tt];
INTEGER: [Ii][Nn][Tt][Ee][Gg][Ee][Rr];
INT: [Ii][Nn][Tt];
BIGINT: [Bb][Ii][Gg][Ii][Nn][Tt];
FLOAT: [Ff][Ll][Oo][Aa][Tt];
REAL: [Rr][Ee][Aa][Ll];
DOUBLE: [Dd][Oo][Uu][Bb][Ll][Ee];
DECIMAL: [Dd][Ee][Cc][Ii][Mm][Aa][Ll];
NUMERIC: [Nn][Uu][Mm][Ee][Rr][Ii][Cc];
DATE: [Dd][Aa][Tt][Ee];
TIME: [Tt][Ii][Mm][Ee];
TIMESTAMP: [Tt][Ii][Mm][Ee][Ss][Tt][Aa][Mm][Pp];
DATETIME: [Dd][Aa][Tt][Ee][Tt][Ii][Mm][Ee];
BOOLEAN: [Bb][Oo][Oo][Ll][Ee][Aa][Nn];
BOOL: [Bb][Oo][Oo][Ll];
BLOB: [Bb][Ll][Oo][Bb];
TEXT: [Tt][Ee][Xx][Tt];
JSON: [Jj][Ss][Oo][Nn];
XML: [Xx][Mm][Ll];
CURRENT_TIMESTAMP: [Cc][Uu][Rr][Rr][Ee][Nn][Tt]'_'[Tt][Ii][Mm][Ee][Ss][Tt][Aa][Mm][Pp];
CAST: [Cc][Aa][Ss][Tt];
EXTRACT: [Ee][Xx][Tt][Rr][Aa][Cc][Tt];
RESTRICT: [Rr][Ee][Ss][Tt][Rr][Ii][Cc][Tt];
CASCADE: [Cc][Aa][Ss][Cc][Aa][Dd][Ee];
NO: [Nn][Oo];
ACTION: [Aa][Cc][Tt][Ii][Oo][Nn];

// Non-reserved words that can be used as identifiers
nonReservedWord
    : TEMP | TEMPORARY | REPLACE | EXISTS | IF
    | CONSTRAINT | COLUMN | DATABASE | INDEX
    | RESTRICT | CASCADE | NO | ACTION
    | CHARACTER | VARCHAR | BINARY | VARBINARY
    | TINYINT | SMALLINT | INTEGER | INT | BIGINT
    | FLOAT | REAL | DOUBLE | DECIMAL | NUMERIC
    | DATE | TIME | TIMESTAMP | DATETIME
    | BOOLEAN | BOOL | BLOB | TEXT | JSON | XML
    | CHARSET | COLLATE | AUTO_INCREMENT | COMMENT
    | ADD | MODIFY | ENGINE
    | DATABASES | TABLES | COLUMNS
    | CREATE_TIME | UPDATE_TIME
    | VERSION
    ;

// Lexer rules
fragment DIGIT: [0-9];
fragment LETTER: [a-zA-Z];
TRUE: [Tt][Rr][Uu][Ee];
FALSE: [Ff][Aa][Ll][Ss][Ee];

INTEGER_VALUE
    : DIGIT+
    ;

DECIMAL_VALUE
    : DIGIT+ '.' DIGIT*
    | '.' DIGIT+
    ;

STRING
    : '\'' ( ~('\''|'\\') | ('\\' .) )* '\''
    | '"' ( ~('"'|'\\') | ('\\' .) )* '"'
    ;

IDENTIFIER
    : (LETTER | '_') (LETTER | DIGIT | '_')*
    ;

BACKQUOTED_IDENTIFIER
    : '`' ( ~'`' | '``' )* '`'
    ;

// Comments and whitespace
SIMPLE_COMMENT
    : '--' ~[\r\n]* '\r'? '\n'? -> channel(HIDDEN)
    ;

BRACKETED_COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;

WS
    : [ \r\n\t]+ -> channel(HIDDEN)
    ;