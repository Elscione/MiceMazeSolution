package solution.model

import solution.model.Node

class SolutionMaze {

    def N
    def E
    def T
    boolean[] visited
    int[] cells
    List<ArrayList<Node>> lists = new ArrayList<>()

    SolutionMaze() {}

    SolutionMaze(int N, int E, int T) {
        this.N = N
        this.E = E
        this.T = T
        visited = new boolean[N]
        cells = new int[N]

        for (int i = 0; i < N; i++) {
            lists.add(new ArrayList<Node>())
        }
    }

    void addEdge(int a, int b, int w) {
        lists.get(b).add(new Node(a, w))
    }

    void dijkstra() {
        PriorityQueue<Node> pq = new PriorityQueue<>(N, new Node())
        for (int i = 0; i < N; i++) {
            visited[i] = false
            cells[i] = Integer.MAX_VALUE
        }

        pq.add(new Node(E, 0))
        cells[E] = 0

        while (!pq.isEmpty()) {
            Node current = pq.remove()
            for (int c = 0; c < lists.get(current.index).size(); c++) {
                Node dest = lists.get(current.index).get(c)
                if (!visited[dest.index] && cells[current.index] + dest.weight < cells[dest.index] && dest.index != current.index) {
                    cells[dest.index] = cells[current.index] + dest.weight
                    pq.add(new Node(dest.index, cells[dest.index]))
                }
            }
            visited[current.index] = true
        }
    }

    int getNumMice() {
        def result = 0
        for (int i = 0; i < N; i++) {
            if (cells[i] <= T) {
                result++
            }
        }
        return result
    }
}
