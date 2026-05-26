import java.util.Random;

public class Crossover {

    public static Random rand =
            new Random();

    public static Node crossover(Node parent1, Node parent2, int maxDepth) {

        if(rand.nextDouble() < 0.5) {

            return parent1;
        }

        return parent2;
    }
}