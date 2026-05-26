public class featureNode extends Node {

    int featureIndex;

    public featureNode(int featureIndex) {
        this.featureIndex = featureIndex;
    }

    @Override
    public double evaluate(Instance instance) {
        return instance.features[featureIndex];
    }

    @Override
    public Node copy() {
        return new featureNode(featureIndex);
    }

    @Override
    public String toString() {
        return "Feature(" + featureIndex + ")";
    }
}