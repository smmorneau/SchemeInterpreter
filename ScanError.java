// ScanError - An exception for parse errors.

public class ScanError extends Exception {
    int position;
    char character;

    public ScanError(int position, char character) {
        this.position = position;
        this.character = character;
    }

    public ScanError(int position, Character c) {
        this.position = position;
    }
    
    public String toString() {
        return "Scan Error at position: " + Integer.toString(position) + " character: " +
                character;
    }
}
