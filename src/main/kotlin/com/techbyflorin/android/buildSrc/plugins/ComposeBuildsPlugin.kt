package com.techbyflorin.android.buildSrc.plugins

import com.google.gson.Gson
import com.techbyflorin.android.buildSrc.models.ModelJson
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import java.io.File

/**
 * Gradle Plugin that applies project components as plugins build dependencies if required by modules.json during
 * gradle initialization phase.
 *
 * Note: modules.json is a json file located at the project root level which describes all the components a project
 * depends on as plugins builds
 */
class ComposeBuildsPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        val moduleJsonFile = "${target.rootDir}/modules.json"

        println("Modules.json found at $moduleJsonFile")

        try {
            val jsonValue = File(moduleJsonFile).readText()
            val modules = Gson().fromJson(jsonValue, ModelJson::class.java)

            println("Projects found = ${modules.projects}")

            modules.projects.forEach { project ->
                when (project.composed) {
                    true -> println("Applying project ${project.name} as composed")
                    else -> println("Applying project ${project.name} as binary depency")
                }

//                target.includeBuild(project)
            }
        } catch (ex: Exception) {
            println("Error parsing modules.json. Error = $ex")
        }
    }
}