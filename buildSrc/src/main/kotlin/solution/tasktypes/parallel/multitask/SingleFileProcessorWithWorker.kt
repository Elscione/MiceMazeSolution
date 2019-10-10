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
import solution.models.TestCase
import java.io.File
import java.util.*
import javax.inject.Inject

open class ParallelMultiTaskSolution @Inject constructor(private val workerExecutor: WorkerExecutor,
                                                         @InputFile val inputFile: File,
                                                         @OutputFile val outputFile: File) : DefaultTask() {

    @TaskAction
    fun execute(inputs: IncrementalTaskInputs) {
        if (!inputs.isIncremental) outputFile.delete()

        val workQueue = workerExecutor.noIsolation()

        inputs.outOfDate {
            val inputFile = it.file
            workQueue.submit(ParallelMultiTaskAction::class.java) {
                it.getInputFile().set(inputFile)
                it.getOutputFile().set(outputFile)
            }
        }

        inputs.removed { it.file.delete() }
    }
}

abstract class ParallelMultiTaskAction: WorkAction<ParallelMultiTaskParameters> {
    override fun execute() {
        val inputFile = parameters.getInputFile().asFile.get()

        if (inputFile.canRead()) {
            processFileInput(inputFile)
        }
    }

    private fun processFileInput(inputFile: File) {
        if (inputFile.canRead()) {
            val outputFile = parameters.getOutputFile().asFile.get()

            outputFile.writeText("")

            val scanner = Scanner(inputFile.readText())

            val nTestCase = scanner.nextLine().trim().toInt()
            scanner.nextLine()

            for (i in 1..nTestCase) {
                val n = scanner.nextLine().trim().toInt()
                val e = scanner.nextLine().trim().toInt()
                val t = scanner.nextLine().trim().toInt()

                val testCase = TestCase(n, e, t)

                val nConnection = scanner.nextLine().trim().toInt()

                for (j in 1..nConnection) {
                    val connectionData = scanner.nextLine().split(' ').map { it.toInt() }.toTypedArray()

                    testCase.graph.createReversedConnection(testCase.graph.nodes[connectionData[0] - 1],
                            testCase.graph.nodes[connectionData[1] - 1],
                            connectionData[2])
                }

                outputFile.appendText(testCase.getResult().toString())
                if (scanner.hasNextLine()) {
                    outputFile.appendText("\n\n")
                    scanner.nextLine()
                }
            }
        }
    }
}

interface ParallelMultiTaskParameters: WorkParameters {
    fun getInputFile(): RegularFileProperty
    fun getOutputFile(): RegularFileProperty
}