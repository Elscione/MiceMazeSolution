package solution.model

class Node implements Comparator<Node> {

    public int index
    public int weight

    Node() {}

    Node(int index, int weight) {
        this.index = index
        this.weight = weight
    }

    int compare(Node node1, Node node2) {
        if (node1.weight > node2.weight) return 1
        if (node1.weight < node2.weight) return -1
        return 0
    }
}
