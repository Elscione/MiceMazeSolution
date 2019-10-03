package solution.tasktypes.parallel

import org.gradle.api.file.RegularFileProperty
import org.gradle.workers.WorkParameters

interface MazeWorkParameters extends WorkParameters {
    RegularFileProperty getInputFile()
    RegularFileProperty getOutputFile()
}