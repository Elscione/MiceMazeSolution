import solution.tasktypes.parallel.ParallelTaskSolution
import solution.tasktypes.parallel.multitask.ParallelMultiTaskSolution
import solution.tasktypes.serial.SerialSolutionTask

val output = File("${buildDir}/outputs")
val files: FileCollection = fileTree("$rootDir/inputs").matching { include { it.name.startsWith("input") } }.filter { it.isFile }

tasks.register<Delete>("clean") {
    delete("build")
}

// create a single task to solve the solution serially
tasks.register<SerialSolutionTask>("solutionTaskInSerial") {
    inputFiles.addAll(files)
    outputDirectory = output
}

// create a single task that solve the solution in parallel
tasks.register<ParallelTaskSolution>("solutionSingleTaskParallel") {
    inputFiles.addAll(files)
    outputDirectory = output
    outputDirectory.mkdirs()
}

// create a single task for each input files and assign the work to a worker executor so that we can run multiple task at once
fileTree("${rootProject.rootDir}/inputs").files.forEach {
    tasks.register<ParallelMultiTaskSolution>(it.name, it, File("${project.buildDir}/outputs/${it.name.replace("input", "output")}"))
}

// create a task that depends on input tasks created above
// so that we can run all the input task by executing this task
tasks.register("multiTask") {
    dependsOn(tasks.matching { it.name.startsWith("input") })
}