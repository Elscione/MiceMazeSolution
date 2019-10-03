package solution.tasktypes.parallel

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor

import javax.inject.Inject

class MazeParallelTaskSolution extends DefaultTask{

    WorkerExecutor workerExecutor
    def countFile = 0

    @InputDirectory
    File inputDirectory

    @OutputDirectory
    def outputDirectory

    @Inject
    ParallelSolutionTask(WorkerExecutor workerExecutor) {
        this.workerExecutor = workerExecutor
    }

    @TaskAction
    void execute(IncrementalTaskInputs inputs) {
        try {
            if (!inputs.isIncremental()) this.outputDirectory.delete()
            if (!outputDirectory.exists()) outputDirectory.mkdirs()

            WorkQueue workQueue = workerExecutor.noIsolation()
            def startTime = System.currentTimeMillis()

            inputs.outOfDate { changedFileDetail ->
                def changeFile = changedFileDetail.file

                if(changeFile.canRead() && changeFile.isFile()) {
                    workQueue.submit(MazeSolutionWorkAction.class) { con ->
                        con.getInputFile().set(changeFile)
                        con.getOutputFile().set(createOutputFile(changeFile))
                    }
                    countFile++
                }
            }

            inputs.removed {
                it.file.delete()
            }

            workerExecutor.await()

            def endTime = System.currentTimeMillis()
            def totalTime = (endTime - startTime) / 1000

            println("$countFile File Execution,  Ellapsed time: $totalTime ms")

        } catch (Exception e) {
            throw new RuntimeException(e)
        }
    }

    def createOutputFile(inputFile) {
        return new File("${outputDirectory.path}/${inputFile.name.replace("input", "output")}")
    }
}
