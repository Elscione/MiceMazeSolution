package solution.tasktypes.parallel

import org.gradle.api.file.RegularFileProperty
import org.gradle.workers.WorkParameters

interface SolutionWorkParameters: WorkParameters {
    fun getInputFile(): RegularFileProperty
    fun getOutputFile(): RegularFileProperty
}