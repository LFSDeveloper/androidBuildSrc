package com.techbyflorin.android.buildSrc.plugins

import com.google.gson.Gson
import com.techbyflorin.android.buildSrc.models.ModelJson
import groovy.util.GroovyTestCase
import junit.framework.TestCase

class ComposeBuildsPluginTest: GroovyTestCase() {

    private val test = "{\n" +
            "  \"projects\": [\n" +
            "    {\n" +
            "      \"name\": \"myLibrary\",\n" +
            "      \"repoUrl\": \"\",\n" +
            "      \"branch\": \"develop\",\n" +
            "      \"composed\": false\n" +
            "    }\n" +
            "  ]\n" +
            "}"

    fun testApply() {
        val loaded = Gson().fromJson(test, ModelJson::class.java)

        TestCase.assertNotNull(loaded)
        TestCase.assertTrue(loaded.projects.isNotEmpty())
    }
}