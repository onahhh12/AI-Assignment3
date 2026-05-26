public class addNode extends Node {

    Node left;
    Node right;

    public addNode(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate(Instance instance) {
        return left.evaluate(instance)
                + right.evaluate(instance);
    }

    @Override
    public Node copy() {
        return new addNode(
                left.copy(),
                right.copy()
        );
    }

    @Override
    public String toString() {
        return "(" + left + " + " + right + ")";
    }
}