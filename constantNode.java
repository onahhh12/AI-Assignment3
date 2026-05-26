public class constantNode extends Node {

    double value;

    public constantNode(double value) {
        this.value = value;
    }

    @Override
    public double evaluate(Instance instance) {
        return value;
    }

    @Override
    public Node copy() {
        return new constantNode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}