import java.util.LinkedList;

/*
 * A scanner implementation for the following token grammar
 *
 * Token     ::= EOT | Ident | Number | '(' | ')' | '#t' | '#f'
 * Ident     ::= NonDigit (Char)*
 * Number    ::= Digit (Digit)*
 * Char      ::= Digit | NonDigit
 * Digit     ::= '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'
 * NonDigit  ::= Letter | Symbol
 * Letter    ::= 'A' ... 'Z' | 'a' ... 'z'
 * Symbol    ::= '=' | '*' | '+' | '/' | '<' | '>' | '!' | '?' | '-'
 *
 * WhiteSpace :== ' ' | '\t' | '\n'
 */

public class TokenScanner {
	String expr;
    int inputLength;
    boolean endOfText = false;
    int position = 0;
    char curChar;
	
	public TokenScanner(String expr) throws ScanError {
		this.expr = expr;
        this.inputLength = expr.length();
        try {
        	curChar = expr.charAt(position);
        } catch(StringIndexOutOfBoundsException e) {
        	throw new ScanError(position, null);
        }
	}
	
	/**
	 * Scan the tokens in given string.
	 * 
	 * @return LinkedList<Token>
	 * @throws ScanError
	 */
	public LinkedList<Token> scan() throws ScanError {
        Token token;
        LinkedList<Token> tokens = new LinkedList<Token>();

        while(true) {
            token = scanToken();
            tokens.add(token);
            if (token.getType() == Token.Type.EOT) {
                break;
            }
        }
        return tokens;	
	}
	
	/**
	 * Scan a token.
	 * 
	 * @return Token
	 */
	Token scanToken() {
        Token token = null;
        char c;

        while (!endOfText) {
            c = curChar;
            
            if (c == ' ' || c == '\t' || c == '\n') {
                /* Whitespace */
                charTake();
                continue;
            } else if (Character.isDigit(c)) {
                /* Number */
                return scanNumber();
            } else if (c == '(') {
                /* Left Paren */
                token = new Token(position, Token.Type.LPAREN, String.valueOf(c));
                charTake();
                return token;
            } else if (c == ')') {
                /* Right Paren */
                token = new Token(position, Token.Type.RPAREN, String.valueOf(c));
                charTake();
                return token;
            } else if (c == '#') {
                /* Boolean */
                return scanBool();
            } else if (isNonDigit(c)) { 
            	/* Identifier */
                return scanIdent();
        	} else {
        		System.out.println("-> curChar = " + c);
                break;
            }
        }

        if (endOfText) {
            token = new Token(position, Token.Type.EOT, "EOT");
        }

        return token;
    }
	
	/**
	 * Get the next character from the inputString
	 */
    void charTake() {
        position += 1;
        if (position < inputLength) {
            curChar = expr.charAt(position);
        } else {
            curChar = 0;
            endOfText = true;
        }
    }

    /**
     * Scan a number as defined in grammar above.
     * 
     * @return Token
     */
    Token scanNumber() {
        int positionInt = position;
        String textInt = String.valueOf(curChar);
        charTake();

        while (Character.isDigit(curChar)) {
            textInt += String.valueOf(curChar);
            charTake();
        }

        return new Token(positionInt, Token.Type.NUMBER, textInt);
    }
    
    /**
     * Scan a boolean as defined in grammar above.
     * 
     * @return Token
     */
    Token scanBool() {
        int positionBool = position;
        String textBool = String.valueOf(curChar);
        charTake();

        Token.Type tokenType = Token.Type.NONE;
        if (curChar == 't') {
        	tokenType = Token.Type.TRUE;
        } else if (curChar == 'f') {
        	tokenType = Token.Type.FALSE;
        } 

        textBool += String.valueOf(curChar);
        charTake();
        return new Token(positionBool, tokenType, textBool);
    }
    
    /**
     * Scan an ident as defined in grammar above.
     * 
     * @return Token
     */
    Token scanIdent() {
        int positionIdent = position;
        String textIdent = String.valueOf(curChar);
        charTake();

        while (isChar(curChar)) {
            textIdent += String.valueOf(curChar);
            charTake();
        }

        return new Token(positionIdent, Token.Type.IDENT, textIdent);
    }

	boolean isChar(char c) {
		return (Character.isDigit(c) || isNonDigit(c));
	}
	
	boolean isNonDigit(char c) {
		return (Character.isLetter(c) || isSymbol(c));
	}
	
	boolean isSymbol(char c) {
		return (c == '=' || c == '*' || c == '+' || c == '/' || c == '<' 
				|| c == '>' || c == '!' || c == '?' || c == '-');
	}
}
