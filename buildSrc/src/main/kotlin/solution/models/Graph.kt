package solution.models

import java.util.*

data class Graph(val nodes: MutableList<Node>, val edges: MutableList<Edge>) {

    fun createReversedConnection(initialNode: Node, endNode: Node, cost: Int) {
        val newEdge = Edge(endNode, initialNode, cost)
        this.edges.add(newEdge)
        endNode.edges.add(newEdge)
    }

    fun getNumOfVisitableNodeWithinTCost(initialNode: Node, maxCost: Int): Int {
        var numOfVisitableNode = 0
        val nodesToVisit = LinkedList<Node>()

        // add exit node as start node
        nodesToVisit.add(initialNode)
        initialNode.totalCost = 0

        // keep traversing as long as there is nodes to visit
        while (!nodesToVisit.isEmpty()) {
            val currentNode = nodesToVisit.poll()

            currentNode.edges.forEach {
                // calculate the cost to walk from initial node to this node
                val newCost = currentNode.totalCost + it.cost

                // update the cost if the new cost is smaller
                if (newCost < it.endNode.totalCost) {
                    it.endNode.totalCost = newCost
                }

                // add the adjacent node to the queue if:
                // 1. if the node is not yet visited
                // 2. if the node is not yet inside nodesToVisit
                // 3. if the calculated totalCost is below threshold
                if (!it.endNode.visited && !nodesToVisit.contains(it.endNode) && it.endNode.totalCost <= maxCost) {
                    nodesToVisit.add(it.endNode)
                }
            }

            // mark current node as visited, so it will not be registered as node to visit
            currentNode.visited = true

            numOfVisitableNode++
        }

        return numOfVisitableNode
    }
}