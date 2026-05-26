import java.util.List;

public class Metrics {

    // Accuracy
    public static double accuracy(Node tree, List<Instance> data) {

        int correct = 0;

        for(Instance inst : data) {

            int prediction = predict(tree, inst);

            if(prediction == inst.label) {
                correct++;
            }
        }

        return (double) correct / data.size();
    }

    // Prediction for a single instance
    public static int predict(Node tree, Instance instance) {

        double output = tree.evaluate(instance);

        return output > 0 ? 1 : 0;
    }

    // F1 score
    public static double f1Score(Node tree, List<Instance> data) {

        int tp = 0;
        int fp = 0;
        int fn = 0;

        for(Instance inst : data) {

            int prediction = predict(tree, inst);

            if(prediction == 1 && inst.label == 1) {

                tp++;
            }

            if(prediction == 1 && inst.label == 0) {

                fp++;
            }

            if(prediction == 0 && inst.label == 1) {

                fn++;
            }
        }

        double precision = tp / (double)(tp + fp + 0.0001);

        double recall = tp / (double)(tp + fn + 0.0001);

        return 2 *((precision * recall) / (precision + recall + 0.0001));
    }
}