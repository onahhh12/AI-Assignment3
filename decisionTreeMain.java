import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class decisionTreeMain {

    static final double PARSIMONY_COEFFICIENT = 0.0003;
    static Random rand = new Random();

    //  Fitness with parsimony pressure 
    static double fitnessWithParsimony(decisionNode tree, List<Instance> data) {
        double f1 = decisionTreeMetrics.f1Score(tree, data);
        int treeSize = countNodes(tree);
        return f1 - (PARSIMONY_COEFFICIENT * treeSize);
    }

    //  Count nodes in a decision tree 
    static int countNodes(decisionNode node) {
        if (node == null) return 0;
        if (node.classLabel != null) return 1;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    //  Get all nodes as a flat list 
    static List<decisionNode> getAllNodes(decisionNode node) {
        List<decisionNode> nodes = new ArrayList<>();
        collectNodes(node, nodes);
        return nodes;
    }

    static void collectNodes(decisionNode node, List<decisionNode> nodes) {
        if (node == null) return;
        nodes.add(node);
        if (node.classLabel == null) {
            collectNodes(node.left, nodes);
            collectNodes(node.right, nodes);
        }
    }

    //  Tournament selection 
    static decisionNode tournament(List<decisionNode> population,
                                   List<Double> fitnesses,
                                   int tournamentSize) {
        decisionNode best = null;
        double bestFit = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < tournamentSize; i++) {
            int idx = rand.nextInt(population.size());
            if (fitnesses.get(idx) > bestFit) {
                bestFit = fitnesses.get(idx);
                best = population.get(idx);
            }
        }
        return best;
    }

    //  Subtree crossover 
    static decisionNode crossover(decisionNode parent1, decisionNode parent2, int maxDepth) {
        decisionNode child = parent1.copy();
        List<decisionNode> childNodes = getAllNodes(child);
        List<decisionNode> donor2Nodes = getAllNodes(parent2);

        if (childNodes.isEmpty() || donor2Nodes.isEmpty()) return child;

        // Pick a random donor subtree from parent2
        decisionNode donor = donor2Nodes.get(rand.nextInt(donor2Nodes.size())).copy();

        // Pick a random node in child to replace — but not the root
        // Find the parent of a random node and replace its child
        List<decisionNode> internalNodes = new ArrayList<>();
        for (decisionNode n : childNodes) {
            if (n.classLabel == null) internalNodes.add(n);
        }

        if (internalNodes.isEmpty()) return donor;

        decisionNode target = internalNodes.get(rand.nextInt(internalNodes.size()));

        if (rand.nextBoolean()) {
            target.left = donor;
        } else {
            target.right = donor;
        }

        return child;
    }

    // Point mutation 
    static decisionNode mutate(decisionNode tree, double mutationRate) {
        List<decisionNode> nodes = getAllNodes(tree);
        for (decisionNode node : nodes) {
            if (rand.nextDouble() < mutationRate) {
                if (node.classLabel != null) {
                    // Flip leaf class label
                    node.classLabel = node.classLabel == 0 ? 1 : 0;
                } else {
                    // Replace feature and threshold
                    node.featureIndex = rand.nextInt(9);
                    node.threshold = rand.nextDouble() * 5;
                }
            }
        }
        return tree;
    }

    //  Main 
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

            decisionNode bestAcrossAllRuns = null;
            double       bestTestAcc       = -1;
            int          bestRunNumber     = -1;

            for (int run = 0; run < numRuns; run++) {

                System.out.println("\n========== RUN " + (run + 1) + " (seed=" + (baseSeed + run) + ") ==========");

                rand.setSeed(baseSeed + run);
                decisionTreeGenerator.rand.setSeed(baseSeed + run);

                long startTime = System.currentTimeMillis();

                // Initial population
                List<decisionNode> population = new ArrayList<>();
                List<Double>       fitnesses  = new ArrayList<>();

                for (int i = 0; i < populationSize; i++) {
                    decisionNode tree = decisionTreeGenerator.generateTree(initialDepth);
                    double fit = fitnessWithParsimony(tree, train);
                    population.add(tree);
                    fitnesses.add(fit);
                }

                decisionNode bestOverall    = population.get(0);
                double       bestOverallFit = fitnesses.get(0);

                // Evolution loop
                for (int gen = 0; gen < generations; gen++) {

                    List<decisionNode> newPop  = new ArrayList<>();
                    List<Double>       newFits = new ArrayList<>();

                    // Elitism
                    int eliteIdx = 0;
                    for (int i = 1; i < fitnesses.size(); i++) {
                        if (fitnesses.get(i) > fitnesses.get(eliteIdx)) eliteIdx = i;
                    }
                    newPop.add(population.get(eliteIdx));
                    newFits.add(fitnesses.get(eliteIdx));

                    // Breed new individuals
                    while (newPop.size() < populationSize) {

                        decisionNode parent1 = tournament(population, fitnesses, tournamentSize);
                        decisionNode parent2 = tournament(population, fitnesses, tournamentSize);
                        decisionNode child;

                        if (rand.nextDouble() < crossoverRate) {
                            child = crossover(parent1, parent2, maxDepth);
                        } else {
                            child = parent1.copy();
                        }

                        child = mutate(child, mutationRate);

                        double fit = fitnessWithParsimony(child, train);
                        newPop.add(child);
                        newFits.add(fit);
                    }

                    population = newPop;
                    fitnesses  = newFits;

                    // Find best in generation
                    int bestIdx = 0;
                    for (int i = 1; i < fitnesses.size(); i++) {
                        if (fitnesses.get(i) > fitnesses.get(bestIdx)) bestIdx = i;
                    }

                    if (fitnesses.get(bestIdx) > bestOverallFit) {
                        bestOverall    = population.get(bestIdx);
                        bestOverallFit = fitnesses.get(bestIdx);
                    }

                    System.out.println("Gen " + gen
                            + " | Fitness: " + String.format("%.4f", fitnesses.get(bestIdx))
                            + " | Tree size: " + countNodes(population.get(bestIdx))
                            + " | F1: " + String.format("%.4f", decisionTreeMetrics.f1Score(population.get(bestIdx), train)));
                }

                long endTime = System.currentTimeMillis();
                double trainAcc = decisionTreeMetrics.accuracy(bestOverall, train);
                double testAcc  = decisionTreeMetrics.accuracy(bestOverall, test);
                double f1Test   = decisionTreeMetrics.f1Score(bestOverall, test);
                int    treeSize = countNodes(bestOverall);

                System.out.println("\n--- RUN " + (run + 1) + " RESULTS ---");
                System.out.println("Tree size:         " + treeSize + " nodes");
                System.out.println("Training Accuracy: " + String.format("%.2f%%", trainAcc * 100));
                System.out.println("Test Accuracy:     " + String.format("%.2f%%", testAcc * 100));
                System.out.println("F1 Score (test):   " + f1Test);
                System.out.println("Runtime:           " + (endTime - startTime) + " ms");
                System.out.println("Best Tree: "         + bestOverall);

                if (testAcc > bestTestAcc) {
                    bestAcrossAllRuns = bestOverall;
                    bestTestAcc       = testAcc;
                    bestRunNumber     = run + 1;
                }
            }

            long overallEndTime = System.currentTimeMillis();
            System.out.println("\n========== FINAL BEST ACROSS ALL 30 RUNS ==========");
            System.out.println("Best Run:          " + bestRunNumber + " (seed=" + (baseSeed + bestRunNumber - 1) + ")");
            System.out.println("Tree size:         " + countNodes(bestAcrossAllRuns) + " nodes");
            System.out.println("Training Accuracy: " + String.format("%.2f%%", decisionTreeMetrics.accuracy(bestAcrossAllRuns, train) * 100));
            System.out.println("Test Accuracy:     " + String.format("%.2f%%", decisionTreeMetrics.accuracy(bestAcrossAllRuns, test) * 100));
            System.out.println("F1 Score (test):   " + decisionTreeMetrics.f1Score(bestAcrossAllRuns, test));
            System.out.println("Best Tree: "         + bestAcrossAllRuns);
            System.out.println("TOTAL PROGRAM TIME: " + (overallEndTime - overallStartTime) + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}