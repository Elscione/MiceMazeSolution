import solution.tasktypes.parallel.ParallelTaskSolution
import solution.tasktypes.parallel.multitask.ParallelMultiTaskSolution
import solution.tasktypes.serial.SerialSolutionTask

val output = File("${buildDir}/outputs")
val files: FileCollection = fileTree("$rootDir/inputs").matching { include { it.name.startsWith("input") } }.filter { it.isFile }
output.mkdirs()

// creating serial solution
tasks.register<SerialSolutionTask>("solutionTaskInSerial") {
    inputFiles.addAll(files)
    outputDirectory = output
}

// creating parallel solution with single task
tasks.register<ParallelTaskSolution>("solutionSingleTaskParallel") {
    inputFiles.addAll(files)
    outputDirectory = output
    outputDirectory.mkdirs()
}

// creating parallel solution with multiple task
fileTree("${project.rootDir}/inputs").files.forEach {
    tasks.register<ParallelMultiTaskSolution>("${it.name}Processor", it, File("${project.buildDir}/outputs/${it.name.replace("input", "output")}"))
}

tasks.register("multiTask") {
    dependsOn(tasks.matching { it.name.endsWith("Processor") })
}