/**
 * Represents the tokens output by the TokenScanner.
 */
class Token {
    int position;
    Token.Type tokenType;
    String text;

    public enum Type {
        EOT("EOT"), IDENT("IDENT"), NUMBER("NUMBER"), LPAREN("("), RPAREN(")"),
        TRUE("#t"), FALSE("#f"), NONE("NONE");

        private String value;

        private Type(String value) {
            this.value = value;
        }
    }

    public Token(int position, Token.Type tokenType, String text) {
        this.position = position;
        this.tokenType = tokenType;
        this.text = text;
    }

    public Type getType() {
        return tokenType;
    }

    public String getText() {
        return text;
    }

    public String toString() {
        return "(" + position + ", " + tokenType.toString() + ", " + text + ")";
    }

    public static void main(String[] args) {
        Token t1 = new Token(0, Token.Type.NUMBER, "111");
        System.out.println("Token: " + t1.toString());
        Token t2 = new Token(4, Token.Type.LPAREN, "(");
        System.out.println("Token: " + t2.toString());
    }
}