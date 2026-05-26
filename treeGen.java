import java.util.Random;

public class treeGen {
    public static Random rand = new Random();

    public static Node randomTerm() {

    if(rand.nextBoolean()) {

        int featureIndex = rand.nextInt(9);

        return new featureNode(
                featureIndex);

    } else {

        double constant = rand.nextDouble() * 10 - 5;

        return new constantNode(
                constant);
    }
}

    public static Node randomFunction( Node left, Node right) {
        int choice = rand.nextInt(4);
        switch (choice) {
            case 0:
                return new addNode(left, right);
            case 1:
                return new subtractNode(left, right);
            case 2:
                return new multiplyNode(left, right);
            default:
                return new protectedDivisionNode(left, right);
            
        }
    }

    public static Node genRandomTree(int maxDepth) {

        //Base Case
        if(maxDepth == 0) {
            return randomTerm();
        }

        if (rand.nextDouble() < 0.3) {
            return randomTerm();
        }

        Node left = genRandomTree(maxDepth - 1);
        Node right = genRandomTree(maxDepth - 1);
        return randomFunction(left, right);
    }
    
}


