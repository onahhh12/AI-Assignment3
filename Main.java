import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static final double PARSIMONY_COEFFICIENT = 0.0002;

    static double fitnessWithParsimony(Node tree, List<Instance> data) {
        double f1 = Metrics.f1Score(tree, data);
        int treeSize = TreeUtils.getAllNodes(tree).size();
        return f1 - (PARSIMONY_COEFFICIENT * treeSize);
    }

    public static void main(String[] args) {

        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter base seed (e.g. 1): ");
            long baseSeed = scanner.nextLong();

            System.out.print("Enter path to training CSV: ");
            String trainPath = scanner.next();

            System.out.print("Enter path to test CSV: ");
            String testPath = scanner.next();

            long overallStartTime = System.currentTimeMillis();

            List<Instance> train = dataLoader.loadData(trainPath);
            List<Instance> test  = dataLoader.loadData(testPath);

            int    populationSize = 200;
            int    generations    = 100;
            int    tournamentSize = 5;
            double crossoverRate  = 0.85;
            double mutationRate   = 0.10;
            int    initialDepth   = 6;
            int    maxDepth       = 12;
            int    numRuns        = 30;

            Individual bestAcrossAllRuns = null;
            int        bestRunNumber     = -1;

            for (int run = 0; run < numRuns; run++) {

                System.out.println("\n========== RUN " + (run + 1) + " (seed=" + (baseSeed + run) + ") ==========");

                treeGen.rand.setSeed(baseSeed + run);

                long startTime = System.currentTimeMillis();

                List<Individual> population = new ArrayList<>();

                for (int i = 0; i < populationSize; i++) {
                    Node tree = treeGen.genRandomTree(initialDepth);
                    Individual ind = new Individual(tree);
                    ind.fitness = fitnessWithParsimony(tree, train);
                    population.add(ind);
                }

                Individual bestOverall = population.get(0);

                for (int gen = 0; gen < generations; gen++) {

                    List<Individual> newPopulation = new ArrayList<>();

                    Individual elite = population.get(0);
                    for (Individual ind : population) {
                        if (ind.fitness > elite.fitness) elite = ind;
                    }
                    newPopulation.add(elite);

                    while (newPopulation.size() < populationSize) {

                        Individual parent1 = Selection.tournament(population, tournamentSize);
                        Individual parent2 = Selection.tournament(population, tournamentSize);
                        Node childTree;

                        if (Math.random() < crossoverRate) {
                            childTree = Crossover.crossover(parent1.tree, parent2.tree, maxDepth);
                        } else {
                            childTree = parent1.tree.copy();
                        }

                        childTree = Mutation.mutate(childTree, maxDepth, mutationRate);

                        Individual child = new Individual(childTree);
                        child.fitness = fitnessWithParsimony(childTree, train);
                        newPopulation.add(child);
                    }

                    population = newPopulation;

                    Individual best = population.get(0);
                    for (Individual ind : population) {
                        if (ind.fitness > best.fitness) best = ind;
                    }

                    if (best.fitness > bestOverall.fitness) bestOverall = best;

                    System.out.println("Gen " + gen
                            + " | Fitness: " + String.format("%.4f", best.fitness)
                            + " | Tree size: " + TreeUtils.getAllNodes(best.tree).size()
                            + " | F1: " + String.format("%.4f", Metrics.f1Score(best.tree, train)));
                }

                long endTime = System.currentTimeMillis();
                double trainAcc = Metrics.accuracy(bestOverall.tree, train);
                double testAcc  = Metrics.accuracy(bestOverall.tree, test);
                double f1Test   = Metrics.f1Score(bestOverall.tree, test);
                int    treeSize = TreeUtils.getAllNodes(bestOverall.tree).size();

                System.out.println("\n RUN " + (run + 1) + " RESULTS ");
                System.out.println("Tree size:         " + treeSize + " nodes");
                System.out.println("Training Accuracy: " + trainAcc);
                System.out.println("Test Accuracy:     " + testAcc);
                System.out.println("F1 Score (test):   " + f1Test);
                System.out.println("Runtime:           " + (endTime - startTime) + " ms");
                System.out.println("Best Tree: "         + bestOverall.tree);

                if (bestAcrossAllRuns == null || testAcc > Metrics.accuracy(bestAcrossAllRuns.tree, test)) {
                    bestAcrossAllRuns = bestOverall;
                    bestRunNumber = run + 1;
                }
            }

            long overallEndTime = System.currentTimeMillis();
            System.out.println("\n THE BEST TREE ACROSS ALL 30 RUNS");
            System.out.println("Best Run:          " + bestRunNumber + " (seed=" + (baseSeed + bestRunNumber - 1) + ")");
            System.out.println("Tree size:         " + TreeUtils.getAllNodes(bestAcrossAllRuns.tree).size() + " nodes");
            System.out.println("Training Accuracy: " + Metrics.accuracy(bestAcrossAllRuns.tree, train));
            System.out.println("Test Accuracy:     " + Metrics.accuracy(bestAcrossAllRuns.tree, test));
            System.out.println("F1 Score (test):   " + Metrics.f1Score(bestAcrossAllRuns.tree, test));
            System.out.println("Best Tree: "         + bestAcrossAllRuns.tree);
            System.out.println("TOTAL PROGRAM TIME: " + (overallEndTime - overallStartTime) + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
