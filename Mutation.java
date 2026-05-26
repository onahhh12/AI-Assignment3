import java.util.Random;

public class Mutation {

    private static Random rand =
            new Random();

    public static Node mutate(
            Node tree,
            int maxDepth,
            double mutationRate) {

        if(rand.nextDouble()
                < mutationRate) {

            return treeGen.genRandomTree(
                    maxDepth);
        }

        return tree;
    }
}