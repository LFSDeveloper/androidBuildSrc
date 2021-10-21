package com.techbyflorin.android.buildSrc.plugins

import com.google.gson.Gson
import com.techbyflorin.android.buildSrc.models.ModelJson
import com.techbyflorin.android.buildSrc.models.Project
import org.eclipse.jgit.api.Git
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
        println("Trying module.json handling.")

        val moduleJsonPath = "${target.rootDir}/modules.json"
        val moduleJsonFile = File(moduleJsonPath)
        if (!moduleJsonFile.exists()) {
            println("module.json file not found")
            return
        }

        // fetch projects from module.json if exist
        val subProjects = getSubProjects(moduleJsonFile.readText())

        // clone projects that wants to be used as compound build
        val composedNames = subProjects.asSequence().filter { it.composed }.map {
            cloneProject(it, target.rootDir)
        }.toList()

        val projectToCompose = composedNames.filter { it.isNotEmpty() }
        projectToCompose.takeIf { it.isEmpty() }?.run {
            println("No Projects to compose")
        }

        // applying composed builds
        projectToCompose.forEach {
            println("Including project $it as compose build")
            target.settings.includeBuild("./$it")
        }
    }

    /**
     * Returns a list of the listed subProject within modules.json
     *
     * @param modulesJson represent modules.json data
     */
    private fun getSubProjects(modulesJson: String): List<Project> {
        return try {
            val modules = Gson().fromJson(modulesJson, ModelJson::class.java)
            modules.projects
        } catch (ex: Exception) {
            println("Error parsing modules.json. Error = $ex")
            listOf()
        }
    }

    /**
     * Clones a [project] if needs to be part of a composed build
     *
     * @return the cloned repo
     */
    private fun cloneProject(project: Project, rootDir: File): String {
        project.isCloneable.takeIf { it } ?: return ""

        val location = "$rootDir/${project.name}"
        val locationFile = File(location)

        // if project already cloned then just return name
        locationFile.exists().takeIf { !it } ?: return project.name

        return try {
            Git.cloneRepository()
                    .setBare(false)
                    .setURI(project.repoUrl)
                    .setBranch(project.branch)
                    .setDirectory(locationFile)
                    .call()

            project.name
        } catch (ex: Exception) {
            println("Error trying to clone ${project.name}. Error = $ex")
            ""
        }
    }
}