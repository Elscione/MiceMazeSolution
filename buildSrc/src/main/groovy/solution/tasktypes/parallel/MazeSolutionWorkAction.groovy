package solution.tasktypes.parallel

import org.gradle.workers.WorkAction
import solution.model.SolutionMaze

abstract class MazeSolutionWorkAction implements WorkAction<MazeWorkParameters> {

    @Override
    void execute() {
        // Define your work action here
        // Read input file
        // Process input file
        // Write result to output file

        try {

            def inputFile = parameters.getInputFile().asFile.get()
            def outputFile = parameters.getOutputFile().asFile.get()

            if (inputFile.canRead()) {

                def scanner = new Scanner(inputFile.text)
                def I = scanner.nextLine().trim().toInteger()
                scanner.nextLine()

                for (int i = 0; i < I; i++) {
                    def N = scanner.nextLine().trim().toInteger()
                    def E = scanner.nextLine().trim().toInteger() - 1
                    def T = scanner.nextLine().trim().toInteger()

                    def solution = new SolutionMaze(N, E, T)

                    def M = scanner.nextLine().trim().toInteger()

                    for (int j = 0; j < M; j++) {
                        String[] split = scanner.nextLine().toString().split(' ')
                        solution.addEdge(split[0].toInteger() - 1, split[1].toInteger() - 1, split[2].toInteger())
                    }

                    solution.dijkstra()

                    outputFile.append(solution.getNumMice().toString())

                    if (scanner.hasNextLine()) {
                        scanner.nextLine()
                        outputFile.append("\n\n")
                    }

                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
    }
}
