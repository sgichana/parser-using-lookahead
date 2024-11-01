# parser-using-lookahead

This project uses a recursive descnet parser for a programming language "hawk".

## Requirements
Python 3.x

## Setup
1. Close repository 
git clone https://github.com/your-username/hawk-parser.git
cd hawk-parser
2. Install dependecies if required:
pip install -r requirements.txt

## Error Handling
The parser generates and immediately reports errors, such as:

1. Undeclared Identifier: Using a variable before it’s declared.
2. Redeclaration: Declaring a variable more than once.
3. Illegal Symbols: Encountering characters not defined in the grammar.
4. Parse Errors: When a required symbol or structure is missing.
