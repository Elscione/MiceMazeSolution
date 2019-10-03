package solution.tasktypes.serial

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import solution.models.TestCase
import java.io.File
import java.util.*

open class SerialSolutionTask : DefaultTask() {

    @InputFiles
    val inputFiles = mutableListOf<File>()

    @OutputDirectory
    var outputDirectory = File("${project.buildDir}/outputs")

    @TaskAction
    fun execute(inputs: IncrementalTaskInputs) {
        if (!inputs.isIncremental) outputDirectory.deleteRecursively()

        outputDirectory.mkdirs()

        inputs.outOfDate {
            processFileInput(it.file)
        }

        inputs.removed {
            it.file.delete()
        }
    }

    private fun processFileInput(file: File) {
        if (file.canRead()) {
            val outputFile = File("${outputDirectory.path}/${file.name.replace("input", "output")}")
            outputFile.writeText("")

            val scanner = Scanner(file.readText())

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

