import java.util.ArrayList;
import java.util.LinkedList;

/*
 * A parser implementation for the following grammar
 *
 * <program>    :: <sexpr_list> EOT
 *
 * <sexpr_list> ::= <sexpr> (<sexpr>)*
 *
 * <sexpr>      ::= IDENT | NUMBER | TRUE | FALSE | '(' <sexpr_list> ')'
 *
 */

public class ParserTree {
	private LinkedList<Token> tokens;
    int inputLength;
    boolean endOfTokens = false;
    int position = 0;
    Token curToken;

	public ParserTree(LinkedList<Token> tokens) {
        this.tokens = tokens;
        inputLength = this.tokens.size();
        endOfTokens = false;
        position = 0;
        curToken = tokens.remove();
	}
	
    void tokenTake() {
        position += 1;
        curToken = tokens.remove();
        if (curToken.getType() == Token.Type.EOT) {
            endOfTokens = true;
        }
    }
    
	Tree.SexprList parse() throws ParseError {
		Tree.SexprList sexprs;
		
		sexprs = parseProgram();
        if (curToken.getType() != Token.Type.EOT) {
            throw new ParseError(curToken);
        }
        
		return sexprs;
		
	}
	
    Tree.SexprList parseProgram() throws ParseError {
    	Token.Type type;
    	Tree.SexprList sexprList;
    	
    	sexprList = parseSexprList();
    	type = curToken.getType();
    	
    	if (type != Token.Type.EOT) {
    		throw new ParseError(curToken);
    	}
    	
    	return sexprList;
    }
	
	Tree.SexprList parseSexprList() throws ParseError {
		
        Token.Type type;
        ArrayList<Tree.Sexpr> sexprList = new ArrayList<Tree.Sexpr>();

        sexprList.add(parseSexpr());
        type = curToken.getType();

        // Loop through zero or more <sexpr> rules
        while (type != Token.Type.EOT) {
        	Tree.Sexpr sexpr = parseSexpr();
        	if (sexpr == null) {
        		break;
        	}
        	sexprList.add(sexpr);
            type = curToken.getType();
        }

        return new Tree.SexprList(sexprList);
        
	}
	
	Tree.Sexpr parseSexpr() throws ParseError {

        Token.Type type;
        Tree.Sexpr sexpr = null;
        
        type = curToken.getType();

        if (type == Token.Type.LPAREN) {
            tokenTake();
            sexpr = parseSexprList();
            type = curToken.getType();
            if (type != Token.Type.RPAREN) {
                throw new ParseError(curToken);
            }
            tokenTake();
        } else if (type == Token.Type.IDENT) {
            sexpr = new Tree.SexprIdent(curToken.getText());
            tokenTake();
        } else if (type == Token.Type.NUMBER) {
            sexpr = new Tree.SexprNumber(Integer.parseInt(curToken.getText()));
            tokenTake();
        } else if (type == Token.Type.TRUE) {
            sexpr = new Tree.SexprBoolean(true);
            tokenTake();
        } else if (type == Token.Type.FALSE) {
            sexpr = new Tree.SexprBoolean(false);
            tokenTake();
        } else {
        	return null;
        }
         
        return sexpr;

	}
	
}
