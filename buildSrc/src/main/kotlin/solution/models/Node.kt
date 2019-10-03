package solution.models

data class Node(val id: Int, var visited: Boolean, val edges: MutableList<Edge>, var totalCost: Int)