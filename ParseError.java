// ParseError - An exception for parse errors.



public class ParseError extends Exception {
    Token token;

    public ParseError(Token token) {
        this.token = token;
    }

    public String toString() {
        return "Parse Error at " + token.toString();
    }

}
