package com.techbyflorin.android.buildSrc.models

import java.io.Serializable

/**
 * Describes the model.json file
 */
data class ModelJson(
        val projects: List<Project>
) : Serializable

/**
 * Describe a project dependency
 *
 * @param name of the project
 * @param repoUrl on which [Project] is located
 * @param branch of stable code
 * @param composed set to true if this [Project] instance needs to be applied as a plugins build
 */
data class Project(
        val name: String,
        val repoUrl: String,
        val branch: String,
        val composed: Boolean = false
) : Serializable