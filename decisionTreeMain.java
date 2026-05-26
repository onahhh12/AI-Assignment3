import java.util.List;

public class decisionTreeMain {

    public static void main(String[] args) {

        try {

            long startTime =
                    System.currentTimeMillis();

            List<Instance> train =
                    dataLoader.loadData(
                            "Breast_train.csv");

            List<Instance> test =
                    dataLoader.loadData(
                            "Breast_test.csv");

            decisionNode bestTree = null;

            double bestFitness = 0;

            // 30 RUNS
            for(int run = 1;
                run <= 30;
                run++) {

                System.out.println(
                        "\nRUN " + run);

                decisionNode current =
                        decisionTreeGenerator
                        .generateTree(5);

                double fitness =
                        decisionTreeMetrics
                        .accuracy(current,
                                train);

                System.out.println(
                        "Training Accuracy: "
                        + fitness);

                if(fitness > bestFitness) {

                    bestFitness = fitness;
                    bestTree = current;
                }
            }

            // FINAL RESULTS
            System.out.println(
                    "\nFINAL RESULTS");

            System.out.println(
                    "Best Tree: ");

            System.out.println(bestTree);

            System.out.println(
                    "Training Accuracy: "
                    + decisionTreeMetrics
                    .accuracy(bestTree,
                            train));

            System.out.println(
                    "Test Accuracy: "
                    + decisionTreeMetrics
                    .accuracy(bestTree,
                            test));

            System.out.println(
                    "F1 Score: "
                    + decisionTreeMetrics
                    .f1Score(bestTree,
                            test));

            long endTime =
                    System.currentTimeMillis();

            System.out.println(
                    "Runtime: "
                    + (endTime - startTime)
                    + " ms");

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}