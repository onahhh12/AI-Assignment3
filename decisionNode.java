public class decisionNode {

    public int featureIndex;
    public double threshold;
    public decisionNode left;
    public decisionNode right;
    public Integer classLabel;

    // LEAF NODE
    public decisionNode(int classLabel) {
        this.classLabel = classLabel;
    }

    // DECISION NODE
    public decisionNode(int featureIndex, double threshold, decisionNode left, decisionNode right) {
        this.featureIndex = featureIndex;
        this.threshold = threshold;
        this.left = left;
        this.right = right;
        this.classLabel = null;
    }

    // CLASSIFY
    public int classify(Instance instance) {
        if (classLabel != null) return classLabel;
        if (instance.features[featureIndex] < threshold)
            return left.classify(instance);
        else
            return right.classify(instance);
    }

    public decisionNode copy() {
        if (classLabel != null) return new decisionNode(classLabel);
        return new decisionNode(featureIndex, threshold, left.copy(), right.copy());
    }

    @Override
    public String toString() {
        if (classLabel != null) return "Class(" + classLabel + ")";
        return "(IF Feature(" + featureIndex + ") < " + threshold + " THEN " + left + " ELSE " + right + ")";
    }
}