package solution.tasktypes.parallel.multitask

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.workers.WorkerExecutor
import solution.model.SolutionMaze

import javax.inject.Inject

class ParallelMultiTaskSolutionGroovy extends DefaultTask {

    def workerExecutor

    @InputFile
    def inputFile

    @OutputFile
    def outputFile

    @Inject
    ParallelMultiTaskSolutionGroovy(WorkerExecutor workerExecutor, inputFile, outputFile) {
        this.workerExecutor = workerExecutor
        this.inputFile = inputFile
        this.outputFile = outputFile
    }

    @TaskAction
    def execute(IncrementalTaskInputs inputs) {
        if(!inputs.isIncremental()) outputFile.delete()

        def workQueue = workerExecutor.noIsolation()

        inputs.outOfDate { outOfDateFile ->
            def inputFileDetail = outOfDateFile.file
            workQueue.submit(ParallelMultiTaskActionGroovy.class) { con ->
                con.getFileInput().set(inputFileDetail)
                con.getFileOutput().set(outputFile)
            }
        }

        inputs.removed { con ->
            con.file.delete()
        }
    }
}

abstract class ParallelMultiTaskActionGroovy implements WorkAction<ParallelMultiTaskParameterGroovy> {

    @Override
    void execute() {
        def inputFile = parameters.getFileInput().asFile.get()
        if (inputFile.canRead()) {
            processFile(inputFile)
        }
    }

    def processFile(File inputFile) {
        if (inputFile.canRead()) {
            def outputFile = parameters.getFileOutput().asFile.get()
            outputFile.write("")

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
                    outputFile.append("\n\n")
                    scanner.nextLine()
                }
            }
        }
    }
}

interface ParallelMultiTaskParameterGroovy extends WorkParameters {
    RegularFileProperty getFileInput()

    RegularFileProperty getFileOutput()
}