package solution.tasktypes.serial

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import solution.model.SolutionMaze

class SerialSolutionTask extends DefaultTask {

    @InputDirectory
    File inputDirectory

    @OutputDirectory
    def outputDirectory

    @TaskAction
    void Execute(IncrementalTaskInputs inputs) {
        if (!inputs.isIncremental()) this.outputDirectory.delete()

        if(!outputDirectory.exists()) this.outputDirectory.mkdir()

        def startTime = System.currentTimeMillis()


        inputs.outOfDate { outOfDateFile ->
            def inputFile = outOfDateFile.file

            if (inputFile.canRead() && inputFile.isFile()) {

                def outputFile = new File("$outputDirectory/${inputFile.name.replace("input", "output")}")
                outputFile.write("")

                def scanner = new Scanner(inputFile.text)

                int I = scanner.nextLine().trim().toInteger()
                scanner.nextLine()

                for (int i = 0; i < I; i++) {
                    def N = scanner.nextLine().trim().toInteger()
                    def E = scanner.nextLine().trim().toInteger() - 1
                    def T = scanner.nextLine().trim().toInteger()

                    def solution = new SolutionMaze(N, E, T)

                    def M = scanner.nextLine().trim().toInteger()

                    for(int j = 0; j < M; j++) {
                        String[] split = scanner.nextLine().toString().split(' ')
                        solution.addEdge(split[0].toInteger() - 1, split[1].toInteger() - 1, split[2].toInteger())
                    }

                    solution.dijkstra()

                    outputFile.append(solution.getNumMice().toString())

                    if(scanner.hasNextLine()) {
                        scanner.nextLine()
                        outputFile.append("\n\n")
                    }

                }
            }
        }

        inputs.removed { change ->
            change.file.delete()
        }

        def endTime = System.currentTimeMillis()
        def totalTime = (endTime - startTime) / 1000
        def sizeDir = inputDirectory.list().size()
        println("$sizeDir File Execution,  Ellapsed time: $totalTime ms")
    }
}
