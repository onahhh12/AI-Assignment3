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
    public decisionNode(
            int featureIndex,
            double threshold,
            decisionNode left,
            decisionNode right) {

        this.featureIndex = featureIndex;
        this.threshold = threshold;

        this.left = left;
        this.right = right;

        this.classLabel = null;
    }

    // CLASSIFY INSTANCE
    public int classify(Instance instance) {

        // LEAF
        if(classLabel != null) {
            return classLabel;
        }

        // DECISION
        if(instance.features[featureIndex]
                < threshold) {

            return left.classify(instance);

        } else {

            return right.classify(instance);
        }
    }

    @Override
    public String toString() {

        // LEAF
        if(classLabel != null) {
            return "Class(" + classLabel + ")";
        }

        return "(IF Feature("
                + featureIndex
                + ") < "
                + threshold
                + " THEN "
                + left
                + " ELSE "
                + right
                + ")";
    }
}