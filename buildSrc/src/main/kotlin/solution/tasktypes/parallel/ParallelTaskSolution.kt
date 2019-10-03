package solution.tasktypes.parallel

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

open class ParallelTaskSolution @Inject constructor(private val workerExecutor: WorkerExecutor) : DefaultTask() {

    @InputFiles
    val inputFiles = mutableListOf<File>()

    @OutputDirectory
    var outputDirectory = File("${project.buildDir}/outputs")

    @TaskAction
    fun execute(inputs: IncrementalTaskInputs) {

        if (!inputs.isIncremental) outputDirectory.deleteRecursively()

        val workQueue = workerExecutor.noIsolation()

        outputDirectory.mkdirs()

        inputs.outOfDate {
            val outOfDateInputFile = it.file

            workQueue.submit(SolutionWorkAction::class.java) { params ->
                params.getInputFile().set(outOfDateInputFile)
                params.getOutputFile().set(File("${outputDirectory.path}/${outOfDateInputFile.name.replace("input", "output")}"))
            }
        }

        inputs.removed { it.file.delete() }
    }
}