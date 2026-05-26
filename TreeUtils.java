import java.util.ArrayList;
import java.util.List;

public class TreeUtils {

    // Getting all the nodes in a tree as a list
    public static List<Node> getAllNodes(Node root) {

        List<Node> nodes = new ArrayList<>();

        traverse(root, nodes);

        return nodes;
    }

    private static void traverse(Node node, List<Node> nodes) {

        nodes.add(node);

        if(node instanceof addNode) {

            addNode n = (addNode) node;

            traverse(n.left, nodes);
            traverse(n.right, nodes);
        }

        if(node instanceof subtractNode) {

            subtractNode n = (subtractNode) node;

            traverse(n.left, nodes);
            traverse(n.right, nodes);
        }

        if(node instanceof multiplyNode) {

            multiplyNode n = (multiplyNode) node;

            traverse(n.left, nodes);
            traverse(n.right, nodes);
        }

        if(node instanceof protectedDivisionNode) {

            protectedDivisionNode n = (protectedDivisionNode) node;

            traverse(n.left, nodes);
            traverse(n.right, nodes);
        }
    }
}