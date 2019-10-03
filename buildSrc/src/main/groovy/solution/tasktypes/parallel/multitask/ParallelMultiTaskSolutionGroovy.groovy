package solution.tasktypes.parallel.multitask

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import org.gradle.workers.WorkerExecutor

import javax.inject.Inject

class ParallelMultiTaskSolutionGroovy extends DefaultTask {

    WorkerExecutor workerExecutor

    @InputFile
    File inputFile

    @OutputFile
    File outputFile

    @Inject
    ParallelMultiTaskSolution(WorkerExecutor workerExecutor, File inputFile, File outputFile) {
        this.workerExecutor = workerExecutor
        this.inputFile = inputFile
        this.outputFile = outputFile
    }

    @TaskAction
    void execute(IncrementalTaskInputs inputs) {
        if(!inputs.isIncremental()) outputFile.delete()

        def workQueue = workerExecutor.noIsolation()

        inputs.outOfDate { outOfDateFile ->
            def inputFileDetail = outOfDateFile.file
            workQueue.submit(ParallelMultiTaskAction.class) { con ->
                con.getInputFile().set(inputFileDetail)
                con.getOutputFile().set(outputFile)
            }
        }

        inputs.removed { con ->
            con.file.delete()
        }
    }
}
