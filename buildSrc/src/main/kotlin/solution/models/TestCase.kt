package solution.models

data class TestCase(private val n: Int, private val e: Int, private val t: Int) {

    val graph: Graph = Graph(mutableListOf(), mutableListOf())

    init {
        for (i in 1..n) graph.nodes.add(Node(i, false, mutableListOf(), Integer.MAX_VALUE))
    }

    fun getResult(): Int {
        return graph.getNumOfVisitableNodeWithinTCost(graph.nodes.get(e - 1), t)
    }
}