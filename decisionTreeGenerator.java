import java.util.Random;

public class decisionTreeGenerator {

    public static Random rand =
            new Random();

    public static decisionNode
    generateTree(int depth) {

        // CREATE LEAF
        if(depth == 0 ||
                rand.nextDouble() < 0.3) {

            int label =
                    rand.nextBoolean() ? 1 : 0;

            return new decisionNode(label);
        }

        int feature =
                rand.nextInt(9);

        double threshold =
                rand.nextDouble() * 5;

        decisionNode left =
                generateTree(depth - 1);

        decisionNode right =
                generateTree(depth - 1);

        return new decisionNode(
                feature,
                threshold,
                left,
                right);
    }
}