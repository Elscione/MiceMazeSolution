package solution.tasktypes.parallel.multitask

import org.gradle.api.file.RegularFileProperty
import org.gradle.workers.WorkParameters

interface ParallelMultiTaskParametersGroovy extends WorkParameters {
    RegularFileProperty getInputFile()
    RegularFileProperty getOutputFile()
}
