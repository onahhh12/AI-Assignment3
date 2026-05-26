import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {

            long overallStartTime = System.currentTimeMillis();

            // Load data
            List<Instance> train = dataLoader.loadData("Breast_train.csv");

            List<Instance> test = dataLoader.loadData("Breast_test.csv");

            // Parameters
            int populationSize = 200;
            int generations = 100;
            int tournamentSize = 5;
            double crossoverRate = 0.85;
            double mutationRate = 0.10;
            int initialDepth = 6;
            int maxDepth = 12;
            int numRuns = 30;
            long baseSeed = 1;

            // runs
            for(int run = 0; run < numRuns; run++) {
                System.out.println("RUN " + (run + 1));

                // set seeds for reproducibility
                treeGen.rand.setSeed(baseSeed + run);

                long startTime = System.currentTimeMillis();

                // Initial Population
                List<Individual> population = new ArrayList<>();

                for(int i = 0; i < populationSize; i++) {

                    Node randomTree = treeGen.genRandomTree(initialDepth);

                    Individual ind = new Individual(randomTree);

                    // Fitness
                    ind.fitness = Metrics.f1Score(randomTree, train);
                    population.add(ind);
                }

                System.out.println("Population Size: " + population.size());

                // Best overall individual
                Individual bestOverall = population.get(0);

                // Evolution Loop
                for(int gen = 0; gen < generations;gen++) {

                    List<Individual> newPopulation = new ArrayList<>();

                    // Elitism
                    Individual elite = population.get(0);

                    for(Individual ind : population) {

                        if(ind.fitness > elite.fitness) {
                            elite = ind;
                        }
                    }

                    // Keep best
                    newPopulation.add(elite);

                    // Create new individuals
                    while(newPopulation.size() < populationSize) {

                        // Selection
                        Individual parent1 = Selection.tournament(population, tournamentSize);
                        Individual parent2 = Selection.tournament(population, tournamentSize);
                        Node childTree;

                        // Crossover
                        if(Math.random() < crossoverRate) {

                            childTree = Crossover.crossover(parent1.tree,parent2.tree, maxDepth);

                        } else {

                            childTree = parent1.tree;
                        }

                        // Mutation
                        childTree = Mutation.mutate(childTree, maxDepth, mutationRate);

                        // Create child
                        Individual child = new Individual(childTree);

                        // Fitness
                        child.fitness = Metrics.f1Score(childTree, train);

                        newPopulation.add(child);
                    }

                    // Update population
                    population = newPopulation;

                    // Find best in current population
                    Individual best = population.get(0);

                    for(Individual ind : population) {

                        if(ind.fitness > best.fitness) {

                            best = ind;
                        }
                    }

                    // Track best overall
                    if(best.fitness > bestOverall.fitness) {

                        bestOverall = best;
                    }

                    System.out.println(
                            "Generation: "
                            + gen
                            + " Best Fitness: "
                            + best.fitness);
                }

                // FINAL RESULTS
                System.out.println(
                        "\nFINAL RESULTS");

                System.out.println(
                        "Best Tree: "
                        + bestOverall.tree);

                System.out.println(
                        "Training Accuracy: "
                        + Metrics.accuracy(
                                bestOverall.tree,
                                train));

                System.out.println(
                        "Test Accuracy: "
                        + Metrics.accuracy(
                                bestOverall.tree,
                                test));

                System.out.println(
                        "F1 Score: "
                        + Metrics.f1Score(
                                bestOverall.tree,
                                test));

                long endTime = System.currentTimeMillis();

                System.out.println(
                        "Runtime: "
                        + (endTime - startTime)
                        + " ms");
            }

            long overallEndTime = System.currentTimeMillis();

            System.out.println(
                    "TOTAL PROGRAM TIME: "
                    + (overallEndTime
                    - overallStartTime)
                    + " ms");

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}