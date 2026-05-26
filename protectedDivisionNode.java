public class protectedDivisionNode
        extends Node {

    Node left;
    Node right;

    public protectedDivisionNode(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate(Instance instance) {

        double numerator = left.evaluate(instance);

        double denominator = right.evaluate(instance);

        // Protected division
        if(Math.abs(denominator) < 0.0001) {
            return 1;
        }

        return numerator / denominator;
    }

    @Override
    public Node copy() {

        return new protectedDivisionNode(left.copy(), right.copy()
        );
    }

    @Override
    public String toString() {

        return "(" + left + " / " + right + ")";
    }
}