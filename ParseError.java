/**
 * An exception for errors during expression parsing.
 */
public class ParseError extends Exception {
    Token token;

    public ParseError(Token token) {
        this.token = token;
    }

    public String toString() {
        return "Parse Error at " + token.toString();
    }

}
