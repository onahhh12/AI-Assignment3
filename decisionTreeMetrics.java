import java.util.List;

public class decisionTreeMetrics {

    // ACCURACY
    public static double accuracy(
            decisionNode tree,
            List<Instance> data) {

        int correct = 0;

        for (Instance inst : data) {

            int prediction = tree.classify(inst);

            if (prediction == inst.label) {
                correct++;
            }
        }

        return (double) correct
                / data.size();
    }

    // F1 SCORE
    public static double f1Score(
            decisionNode tree,
            List<Instance> data) {

        int tp = 0;
        int fp = 0;
        int fn = 0;

        for (Instance inst : data) {

            int prediction = tree.classify(inst);

            if (prediction == 1 &&
                    inst.label == 1) {

                tp++;
            }

            if (prediction == 1 &&
                    inst.label == 0) {

                fp++;
            }

            if (prediction == 0 &&
                    inst.label == 1) {

                fn++;
            }
        }

        double precision = tp / (double) (tp + fp + 0.0001);

        double recall = tp / (double) (tp + fn + 0.0001);

        return 2 *
                ((precision * recall)
                        / (precision + recall + 0.0001));
    }
}