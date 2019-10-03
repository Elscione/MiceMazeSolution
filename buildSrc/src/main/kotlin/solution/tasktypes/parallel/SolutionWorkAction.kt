package solution.tasktypes.parallel

import org.gradle.workers.WorkAction
import solution.models.TestCase
import java.io.File
import java.util.*

abstract class SolutionWorkAction: WorkAction<SolutionWorkParameters> {
    override fun execute() {
        val inputFile = parameters.getInputFile().asFile.get()

        if (inputFile.canRead()) {
            processFileInput(inputFile)
        }
    }

    private fun processFileInput(file: File) {
        if (file.canRead()) {
            val outputFile = parameters.getOutputFile().asFile.get()

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