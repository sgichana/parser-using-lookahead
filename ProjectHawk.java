
package projecthawk;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

               
public class ProjectHawk{
   
    public static int lookahead; //match input to the next token
     
    public static HashMap<String, String> symbolTable = new HashMap<>();
    public static int charClass;
    public static char[] lexeme= new char[100];
    public static char nextChar;
    public static int lexLen;
    public static int nextToken;
    public static FileInputStream fileInputStream;
    public static InputStreamReader inputStreamReader;

   
    //Character classes 
    public static final int LETTER = 1;
    public static final int DIGIT = 2;
    public static final int UNKNOWN = 299;
    
    //Token codes 
    public static final int INT_LIT = 31; //integer literal
    public static final int INT_ID = 32;
    public static final int FLOAT_ID = 33; //integer literal
    public static final int DOUBLE_ID = 34;
    public static final int DEC_LIT = 35;
    public static final int IDENT = 36;  //Identifier 
    
    
    public static final int SEMICOLON = 37;
    public static final int COMMA = 38;
    public static final int COLON = 39;
    
   
   
    //RESERVED WORDS
    public static final int PROGRAM = 40;
    public static final int BEGIN = 41;
    public static final int END = 42;
    public static final int IF = 43;
    public static final int THEN = 44;
    public static final int ELSE = 45;
    public static final int INPUT = 46;
    public static final int OUTPUT = 47;
    public static final int WHILE = 48;
    public static final int LOOP = 49;
    
    //OPERATORS
    public static final int ASSIGNMENT =  50;
    public static final int LESS_THAN = 51;
    public static final int GREATER_THAN = 52;  
    public static final int NOT_EQUALS = 53;
    public static final int EQUALS = 54;
    public static final int  ADD = 55;  
    public static final int SUB_OP = 56;    
    public static final int MULT_OP = 57;   
    public static final int DIV_OP = 58;    
    public static final int LEFT_PAREN = 59;
    public static final int RIGHT_PAREN = 60;
   
    public static final int EOF = -1;
    public static int lineNumber = 0;
    
 
   public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the file path: ");
        String filePath = scanner.nextLine();
        fileInputStream = new FileInputStream(filePath);
        inputStreamReader = new InputStreamReader(fileInputStream);
        
       try {
        getChar();
        
        do{
            lexeme = new char[100]; //reset lexeme
            lookahead=lex();
            program();
            lex();
        }
        while(nextToken != EOF);
            inputStreamReader.close();
            fileInputStream.close();
     
        }

        catch (IOException e){
            System.err.println("Error reading the file - " + e.getMessage());
        } 
      catch (RuntimeException e) 
      {
     
        } 
       finally {
           
        scanner.close();
        }
   }

   /*******************************************************************************/
    /* ReservedWordLookup -a function to lookup if lexeme is a reserved word */
    public static int ReservedwordLookup(String lexeme){
        switch (lexeme) {
        case "program":
            return PROGRAM;
        case "begin":
            return BEGIN;
        case "end":
            return END;
        case "if":
            return IF;
        case "then":
            return THEN;
        case "else":
            return ELSE;
        case "while":
            return WHILE;
        case "loop":
            return LOOP;
        case "input":
            return INPUT;
        case "output":
            return OUTPUT;
        case "int":
            return INT_ID;
        case "float":
            return FLOAT_ID;
        case "double":
            return DOUBLE_ID;
        default:
            return IDENT; // if not reserved, its  an identifier
       }
    }
/****************************************************************/
    /*lookup - a function to lookup operators and parentheses
    and return the token */

    public static int lookup(char ch) {
    switch (ch) {
        case '(':
            addChar();
            nextToken = LEFT_PAREN;
            break;
        case ')':
            addChar();
            nextToken = RIGHT_PAREN;
            break;
        case '-':
            addChar();
            nextToken = SUB_OP;
            break;
        case '*':
            addChar();
            nextToken = MULT_OP;
            break;
        case '+':
            addChar();
            nextToken = ADD;
            break;    
        case '/':
            addChar();
            nextToken = DIV_OP;
            break;
        case '<':
            addChar();
            nextToken = LESS_THAN;
            break;
        case '=':
            addChar();
            nextToken = EQUALS;
            break;    
        case '>':
            addChar();
            nextToken = GREATER_THAN;
            break;
        case ';':
            addChar();
            nextToken = SEMICOLON;
            break;    
        case ':':  
            addChar();
            nextToken = COLON;
            break;
        case ',':
            addChar();
            nextToken = COMMA;
            break;

        default:
            addChar();
            nextToken = UNKNOWN;
            break;
    }
    return nextToken;
}
/****************************************************************/
   
    // addChar - a function to add nextChar to lexeme 
    public static void addChar() {
        if (lexLen <= 98) {
        lexeme[lexLen++] = nextChar;
        lexeme[lexLen] = 0;
        }else
            System.out.print("Error - lexeme is too long \n");
    }
  
    public static void getChar() throws IOException {
    int nextCharValue;
    try {
        if ((nextCharValue = fileInputStream.read()) != -1) {
            nextChar = (char) nextCharValue;
            if (Character.isAlphabetic(nextChar))
                charClass = LETTER;
            else if (Character.isDigit(nextChar))
                charClass = DIGIT;
            else 
                charClass = UNKNOWN;
        } else {
            charClass = EOF;
            
        }
        if (nextChar == '\n') {
            lineNumber++;
        }
    } catch (IOException e) {
        System.err.println("Error reading file: " + e.getMessage());
        throw e; // Re-throw the exception to propagate it up the call stack
    }
}

 /****************************************************************/ 
    // getNonBlank - a function to call getChar until it
    //returns a non-whitespace character 
    public static void getNonBlank() throws IOException {
        while ((Character.isWhitespace(nextChar) || nextChar == 0) && charClass != EOF)
            getChar();
        lexeme = new char[100]; // Reset lexeme after skipping whitespace
    }
   
    // lex - a simple lexical analyzer for arithmetic  expressions 
/****************************************************************/
public static String lexemeToString() {
    String lexemeStr = String.valueOf(lexeme); // Trim the lexeme string
    return lexemeStr;
}
/****************************************************************/
public static int lex() throws IOException {
    lexLen = 0;
    getNonBlank();
    switch (charClass) {
        case LETTER:
            addChar();
            getChar();
            while (charClass == LETTER || charClass == DIGIT || nextChar == '_') {
                addChar();
                getChar();
            }
            String lexemeStr = String.valueOf(lexeme).trim(); // Trim the lexeme string
            nextToken = ReservedwordLookup(lexemeStr); 
            if (nextToken == -1) {
                nextToken = IDENT; // If not a reserved word, it's an identifier
            }
            break;
        // Parse integers/decimals literals 
        case DIGIT:
            boolean isDecimal = false;
            int digitCount = 0; // Initialize the digit count
            addChar();
            getChar();
            while (charClass == DIGIT || (nextChar == '.' && !isDecimal)) {
                if (nextChar == '.') {
                    isDecimal = true;
                }
                if (digitCount < 10) { //0 to 9
                    addChar();
                } else {
                    // when digit greater than 10 integers
                    System.err.println("Error - More 10 digits");
                    System.exit(0); // Exit the program
                }
                digitCount++; // digit counter 0-9
                getChar();
            }
            if (isDecimal) {
                nextToken = DEC_LIT; //if its decimal its a flat
            } else {
                nextToken = INT_LIT; // 
            }
            break;
        // Parentheses and operators 
            
        case UNKNOWN:
            switch (nextChar) {
                case ':': //if its a : then check if it is assignment operator or if its just :
                    addChar();
                    getChar();
                    if (nextChar == '='){
                        addChar();
                        nextToken = ASSIGNMENT;
                        getChar();
                    } else {
                        nextToken = COLON;
                    }
                    break;
                case '<': //if its a < then check if it is a not_equals or if its just <
                    addChar();
                    getChar();
                    if (nextChar == '>') {
                        addChar();
                        nextToken = NOT_EQUALS;
                        getChar();
                    } else {
                        nextToken = LESS_THAN;
                    }
                    break;
                default:
                    nextToken = lookup(nextChar); // Call lookup for other unknown characters
                    getChar();
                    break;
            }
            break;
            
        
        case EOF: // reached EOF 
            nextToken = EOF;
            lexeme[0] = 'E';
            lexeme[1] = 'O';
            lexeme[2] = 'F';
            lexeme[3] = 0;
            break;
            
            
    } //end switch
    //System.out.println("Next token is: "+ nextToken + "     \nNext lexeme is:    "+ nextChar + " \n" );
    // System.out.println("Next token: " + nextToken);   //check token
    return nextToken;
    
  }

 /*****************PARSER***********************************************/   
    // Rule 1
    public static void program() throws IOException {
        switch (nextToken) {
            case PROGRAM: // print program, get the next token
                System.out.println("PROGRAM");
                lookahead = lex();
                if (lookahead == IDENT) { //if it is an identifier, then we are in a decl sec
                    decl_sec();
                }

                if (lookahead == BEGIN) { //if it is begin, then we are in the stmt section
                    lookahead = lex(); //get the first token after begin
                    stmt_sec(); 
                } else { //otherwise, check if it is an unknown
                    if (lookahead != UNKNOWN){ //if it isnt, then we are missing begin
                        beginError();
                    } else {
                        UnknownTokenError(); //if it is unknown, then error
                    }
                }
                
                break;
            case UNKNOWN: //if the first token is an unknown, then error
                UnknownTokenError();
                break;
            default: //otherwise, we are missing "program"
                
                MissingProgramError();
             
                break;
        }
    }//end program()
 /**************************************************************************************/
    //Rule 2 
    public static void decl_sec() throws IOException {
    while (lookahead == IDENT) { //while we have an identifier, we are in a decl section, then call decl
        System.out.println("DECL_SEC");
        decl();
    }
    }
    /**********************************************************************************/
    // Rule 3 
    public static void decl() throws IOException {
        System.out.println("DECL"); 
        if (lookahead == IDENT) { 
            String identifier = lexemeToString().trim(); 
            if (symbolTable.containsKey(identifier)) { //if this identifier is in the symbol table, then error
                throw new RuntimeException("Error - Variable has laready been declared : " + identifier);
            } else {  
                //add identify to table with null
                symbolTable.put(identifier, null); 
            }
            id_list();//call id_list
            if (lookahead == COLON) { 
                lookahead = lex(); 
                String type = type(); 
                //save type function to symbol table
                for (String key : symbolTable.keySet()) { 
                    if (symbolTable.get(key) == null) {
                        symbolTable.put(key, type); 
                    }
                }
                if (lookahead != SEMICOLON) { //if next token is not a semi colon, then error
                    StmtTerminatorError();
                }
                lookahead = lex(); //otherwise, get the next token
            }
        } else if (lookahead != UNKNOWN) {        } 
        else {
            //if token is unknown, then error
            UnknownTokenError();
        }
    }//end decl()
   
    /*************************************************************************************/
    // Rule 4 
    public static void id_list() throws IOException {
        System.out.println("ID_LIST");
        switch (lookahead) { 
            case IDENT: //if the next token is an identifier, then get the identifier 
                String identifier = lexemeToString().trim(); 
                symbolTable.put(identifier, null); // Add the identifier to the symbol table with a null type initially 
                lookahead = lex();
                if (lookahead == COMMA) {
                    lookahead = lex();
                    id_list();
                }
                break;
            case UNKNOWN:
                UnknownTokenError();
            default:
                InvalidIDError();
        }
    }//end id_list()
 /***********************************************************************************************/
    // RULE 6 
    public static void stmt_sec() throws IOException {
        //if end or else is found, call stmt
        while (lookahead != END && lookahead != ELSE){ 
            System.out.println("STMT_SEC");
            stmt();
        }
        //return else or end
        if (lookahead == ELSE || lookahead == END){ 
            return;
        }

        if (lookahead != IF && lookahead != SEMICOLON){ //if we return and it is not If or ;, then its an error
            StmtTerminatorError();
        }
    }
    /****************************************************************/
    // RULE 7 
    public static void stmt() throws IOException {
        switch (lookahead) {
            case IDENT: //if its an identifier, then we are in an assignment
                System.out.println("STMT");
                assign();
                break;
            case IF: //if it is If then we are in an if stmt
                System.out.println("STMT");
                ifstmt();
                break;
            case WHILE: //if it is While then we are in while stmt
                System.out.println("STMT");
                whilestmt();
                break;
            case INPUT: //if it is Input then we are in an input
                System.out.println("STMT");
                input();
                break;
            case OUTPUT: //if it is Output, then we are in output
                System.out.println("STMT");
                output();
                break;
            case UNKNOWN: //if it is unknown, then error
                UnknownTokenError();
                break;
            default: //otherwise, it is an invalid statement
                InvalidStmtError();
                break;
        }
        if (lookahead == SEMICOLON) { // checks ; statement terminator
            lookahead = lex();
            if (lookahead == END){
                return;
            }
            stmt_sec();
        } else if (lookahead == UNKNOWN){
             UnknownTokenError();
        } else{
            StmtTerminatorError();
        }
        
    }//end stmt()
    
  /****************************************************************/ 
    // RULE 8 
    public static void assign() throws IOException {
        System.out.println("ASSIGN");
        if (lookahead == IDENT){ //if its an identifier, then get the next one
            lookahead = lex();
            if (lookahead == UNKNOWN) { //check if it is unknown
                UnknownTokenError();
            }
            if (lookahead == ASSIGNMENT){ //check if it is an assignment operator
                lookahead = lex(); //get next token
                expr(); //call expr
            }else { //otherwise, we are missing the assignment operator
                throw new RuntimeException("Error at line " + lineNumber + ": Missing assignment operator");
            }
        } else { //otherwise we are missing an identifier
            throw new RuntimeException("Error at line " + lineNumber + ": missing an identifer in assignment");
        }
    }//end assign()
    
   
    /****************************************************************/
    // RULE 9 
    public static void ifstmt() throws IOException {
        System.out.println("IF_STMT"); 
        lookahead = lex(); //get token after if
        if (lookahead == UNKNOWN) { //check if unknown
            UnknownTokenError();
        }
        if (lookahead == LEFT_PAREN) { //check if (, then call comp
            comp(); 
            if (lookahead == UNKNOWN) { //if it returns an unknown, then error
                UnknownTokenError();
            }
            if (lookahead == THEN) { //check if THEN, then call stmt_sec
                lookahead = lex();
                stmt_sec();
                if (lookahead == ELSE) {
                    lookahead = lex();
                    stmt_sec();
                } //check if end
                if (lookahead != END) {
                    throw new RuntimeException("Error at line " + lineNumber + ": Missing keyword 'end'");
                }
                lookahead = lex(); // Move to the next token after encountering 'end'
                if (lookahead != IF){ //check if if
                    throw new RuntimeException("Error at line " + lineNumber + ": Missing keyword 'if'");
                }
                lookahead = lex();
                if (lookahead != SEMICOLON) { //check if semi_colon is missing
                    StmtTerminatorError();
                }
            } else { //othewise we are missing keyword then
                
                throw new RuntimeException("Error at line " + lineNumber +": Missing keyword 'then");
            }
        } else { //otherwise we are missing a comparison
            throw new RuntimeException("Error at line " + lineNumber +": Missing comparison");
        }
    }//end ifstmt()
    
 /****************************************************************/   
    // RULE 10 
    public static void whilestmt() throws IOException {
        System.out.println("WHILE_STMT");
        lookahead = lex(); //get token after While
        if (lookahead == LEFT_PAREN){ //check if (
            comp(); //call comp
            if (lookahead == LOOP){ //check if comp returns "loop"
                lookahead = lex(); //get token after "loop"
                stmt_sec(); 
                if (lookahead != END){ //if stmt_sec does not return end, then error
                     throw new RuntimeException("Error at line " + lineNumber + ": Missing keyword 'end");
                }
                lookahead = lex(); // Move to the next token after encountering 'end'
                if (lookahead != LOOP){// if next token is not "loop", then error
                    if (lookahead == UNKNOWN){
                         UnknownTokenError();
                    } else{
                        throw new RuntimeException("Error at line " + lineNumber + ": Missing keyword 'loop");
                    }
                }
                lookahead = lex(); //get next token
                if (lookahead != SEMICOLON) { //check for semi_colon
                    StmtTerminatorError();
                }
            } else {
                if (lookahead == UNKNOWN){
                     UnknownTokenError();
                } else{
                    throw new RuntimeException("Error at line " + lineNumber +": Missing keyword 'loop");
                }
            }
        } else {
            throw new RuntimeException("Error at line " + lineNumber +": Missing comparison");
        }
    }//end whilestmt()
 /**********************************************************************************************/   
    // RULE 11 
    public static void input() throws IOException {
        System.out.println("INPUT"); 
        lookahead = lex();
        id_list(); 
    }
    
    /**********************************************************************************************/   
    // RULE 12
    public static void output() throws IOException {
        System.out.println("OUTPUT");
        lookahead = lex(); 
        if (lookahead == IDENT){ //check if its an id
            id_list(); //call id_list
        }
        else if (lookahead == INT_LIT || lookahead == DEC_LIT){ //otherwise, check if its a valid digit
            lookahead = lex(); //get next token
        } else {
            throw new RuntimeException("Error at line " + lineNumber +": invalid output");
        }
    }
   /**********************************************************************************************/    
    // Rule 13 
    public static void expr() throws IOException {
        System.out.println("EXPR"); 
        factor(); //call factor to get operand
        if (lookahead == ADD || lookahead == SUB_OP) { //if factor returns a valid operator, then call expr
            lookahead = lex();
            expr();
        }
    }
/**********************************************************************************************/   
    // Rule 14
    public static void factor() throws IOException {
        System.out.println("FACTOR");
        operand(); //call operand
        if (lookahead == MULT_OP || lookahead == DIV_OP) { //check if operand returns a valid operator, then call factor
            lookahead = lex();
            factor();
        }
    }
/**********************************************************************************************/   
    // Rule 15 
    public static void operand() throws IOException {
        System.out.println("OPERAND");
        switch (lookahead) {
            case INT_LIT:
            case DEC_LIT:
            case IDENT:
                //check if its digit or id
                lookahead = lex();
                break;
            case LEFT_PAREN:
                //otherwise check if its an expr
                lookahead = lex();
                expr();
                if (lookahead == RIGHT_PAREN) { // check if expr with )
                    lookahead = lex();
                } else {
                    throw new RuntimeException("Error at line " + lineNumber +": Missing ')'");
                }   break;
            default:
                //check if it is unknown
                if (lookahead == UNKNOWN) { UnknownTokenError();  }
                
                throw new RuntimeException("Error at line " + lineNumber +": Invalid operand");   }
    }// end operand()
  /**********************************************************************************************/     
    // Rule 17 
    public static void comp() throws IOException {
        System.out.println("COMP");
        if (lookahead == LEFT_PAREN) { //check if its (
            lookahead = lex();
            operand(); //give next token operand
            if (lookahead == LESS_THAN || lookahead == EQUALS || lookahead == GREATER_THAN || lookahead == NOT_EQUALS) { //check if valid comp
                lookahead = lex();
                operand(); 
                if (lookahead == RIGHT_PAREN) { //check if valid comp with )
                    lookahead = lex();
                } else {
                    
                    throw new RuntimeException("Error at line " +lineNumber +": Missing right_paren ')'");}
            } else {
                if (lookahead == UNKNOWN){
                     UnknownTokenError();
                }else {
                    throw new RuntimeException("Error at line " + lineNumber +": Missing left_paren  '('");}
            }
        } else {
            throw new RuntimeException("Error at line " + lineNumber +": Not a valid comparison operator"); }
    } //end comp()
    
 /**********************************************************************************************/      
    // RULE 18 (*)  <Need type class for symbol table>
    public static String type() throws IOException { //check if valid type, otherwise error
        String type = null;
        if (lookahead == INT_ID || lookahead == FLOAT_ID || lookahead == DOUBLE_ID) {
            type = lexemeToString().trim();
            lookahead = lex(); // move to the next token
        } else {   InvalidTypeError();  }
        return type;
    }// end type()   
    
 //Errors   
 /**********************************************************************************************/      
    public static void StmtTerminatorError() { //error - missing ;
        throw new RuntimeException("Error at line " + lineNumber +": Semicolon ';' is missing");
    }
    public static void beginError() { //error - missing 'begin'
        throw new RuntimeException("Error at line " + lineNumber +": begin is missing");
    }
    public static void UnknownTokenError() { //error - unknown token
        throw new RuntimeException("Error at line " + lineNumber +": Unknown token");
    }
    public static void MissingProgramError() { //error - missing 'program'
        throw new RuntimeException("Error at line " + lineNumber +": program is missing");
    }
    public static void InvalidIDError() { //error - an invalid id 
        throw new RuntimeException("Error at line " + lineNumber +": Invalid identifier");
    }
    public static void InvalidTypeError() { //error - invalid type
       throw new RuntimeException("Error at line " +lineNumber +": Invalid type");
    }
    public static void InvalidStmtError() { //error - inavlid statement
        throw new RuntimeException("Error at line " + lineNumber +": Invalid statement");
    }
    

}

