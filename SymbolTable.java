
package projecthawk;

/**
  PROJECT HAWK
  * **************************************************************************
Rule 01: PROGRAM a program DECL_SEC begin STMT_SEC end; |
program begin STMT_SEC end;
Rule 02: DECL_SEC a DECL | DECL DECL_SEC
Rule 03: DECL a ID_LIST : TYPE ;
Rule 04: ID_LIST a ID | ID , ID_LIST
Rule 05: ID (a | b | c | ... | z | A | ... | Z) ( a | b | ... | z | A |
... | Z | 0 | 1 | ... | 9)
Rule 06: STMT_SEC a STMT | STMT STMT_SEC
Rule 07: STMT a ASSIGN | IFSTMT | WHILESTMT | INPUT | OUTPUT
Rule 08: ASSIGN a ID := EXPR ;
Rule 09: IFSTMT a if COMP then STMT_SEC end if ; |
if COMP then STMT_SEC else STMT_SEC end if ;
Rule 10: WHILESTMT a while COMP loop STMT_SEC end loop ;
Rule 11: INPUT a input ID_LIST;
Rule 12: OUTPUT a output ID_LIST | output NUM;
Rule 13: EXPR a FACTOR | FACTOR + EXPR | FACTOR - EXPR
Rule 14: FACTOR a OPERAND | OPERAND * FACTOR | OPERAND / FACTOR
Rule 15: OPERAND a NUM | ID | ( EXPR )
Rule 16: NUM a (0 | 1 | ... | 9)+ [.(0 | 1 | ... | 9)+]
Rule 17: COMP a ( OPERAND = OPERAND ) | ( OPERAND <> OPERAND ) |
( OPERAND > OPERAND ) | ( OPERAND < OPERAND )
Rule 18: TYPE a int | float | double
This grammar has 18 rules. It also has reserved words in it indicating that they cannot be used
for identifiers. Non-terminal symbols are those that are capitalized, and terminal symbols are
those that are lowercase. Many rules have alternative choices, for example Rule 09 has two
alternatives, one without an else, and one with and else.
The following are the lexemes for the language:
• Reserved words: program, begin, end, if, then, else, input, output, int, while, loop.
• Operators: assignment (:=), less than (<), greater than (>), equals (=), not equals (<>),
plus (+), minus (-) , multiply (*), divide (/) and parentheses.
• The ‘;’ is also used to terminate statements and the ‘,’ and the ‘:’ are used when
declaring variables.
• Identifiers: start with a letter or an ‘_’ followed by any number of letters, digits or
underscores.
• Numbers: Either integer numbers (max 10 digits), or floating point numbers (max 10
digits).
                                         
 *****************************************************************************************/
import java.util.HashMap;
public class SymbolTable {


    private final HashMap<String, String> symbolTable;

    public SymbolTable() {
        this.symbolTable= new HashMap<>();
    }

    public void insert(String identifier, String type) {
        symbolTable.put(identifier, type);
    }

    public void display() {
        System.out.println("Symbol Table:");
        for (String key : symbolTable.keySet()) {
            System.out.println("Identifier: " + key + ", Type: " + symbolTable.get(key));
        }
    }
}

    

