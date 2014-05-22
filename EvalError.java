/**
 * An exception for expression evaluation errors.
 */
public class EvalError extends Exception {
    Tree.Node node;

    public EvalError(Tree.Node node) {
        this.node = node;
    }

    public String toString() {
        return "Eval Error at subtree: " + node.toString();
    }
}
