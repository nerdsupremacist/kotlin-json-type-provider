package org.jetbrains.kotlin.script.examples.json

import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.reflect.full.memberProperties
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvm.impl.getResourcePathForClass
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

class ScriptTest {

    @Test
    fun `Reading greeting from simple outputs hello world`() {
        val out = captureOut {
            val res = evalFile("simple.json.kts")
            assertSucceeded(res)
        }

        Assert.assertEquals("hello world", out)
    }

    @Test
    fun `Importing the same file twice is allowed`() {
        val out = captureOut {
            val res = evalFile("imported-twice.json.kts")
            assertSucceeded(res)
        }

        Assert.assertEquals("hello world", out)
    }

    @Test
    fun `Importing the same type from different files is allowed`() {
        val out = captureOut {
            val res = evalFile("imported-twice-from-different-folders.json.kts")
            assertSucceeded(res)
        }.lines()

        Assert.assertEquals("hello world", out[0])
        Assert.assertEquals("sup?", out[1])
    }

    @Test
    fun `Importing two clashing files fails`() {
        val res = evalFile("name-clash.json.kts")
        Assert.assertTrue(res is ResultWithDiagnostics.Failure)

        val error = res.reports.first { it.severity == ScriptDiagnostic.Severity.ERROR }
        Assert.assertEquals("Multiple definitions of `SimpleImpl`", error.message)
    }

    @Test
    fun `Specifying a Type Name avoids clash`() {
        val out = captureOut {
            val res = evalFile("name-clash-avoided.json.kts")
            assertSucceeded(res)
        }.lines()

        Assert.assertEquals("hello world", out[0])
        Assert.assertEquals("info", out[1])
    }

    @Test
    fun `Property is mapped to Any? when it only appears as null`() {
        val out = captureOut {
            val res = evalFile("no-info.json.kts")
            assertSucceeded(res)
        }.lines()

        Assert.assertEquals("parsed.info=null", out[0])
        Assert.assertEquals("parsed.info.type=kotlin.Any?", out[1])
    }

    @Test
    fun `Property is mapped to optional when it is null in some members of an array`() {
        val out = captureOut {
            val res = evalFile("with-null.json.kts")
            assertSucceeded(res)
        }.lines()

        Assert.assertEquals("parsed.info=[42, null, 1337, null]", out[0])
        Assert.assertEquals("parsed.info.type=kotlin.collections.List<kotlin.Int?>", out[1])
    }

    @Test
    fun `Property is mapped to optional when it is missing in some members of an array`() {
        val out = captureOut {
            val res = evalFile("with-missing-keys.json.kts")
            assertSucceeded(res)
        }.lines()

        Assert.assertEquals("parsed.values.info=[42, null, 1337, null]", out[0])
        Assert.assertEquals("parsed.values.info.type=kotlin.Int?", out[1])
    }

    @Test
    fun `Property is mapped to Any when conflicting types where used`() {
        val out = captureOut {
            val res = evalFile("with-conflicting-types.json.kts")
            assertSucceeded(res)
        }.lines()

        Assert.assertEquals("parsed.info=[42, 1337]", out[0])
        Assert.assertEquals("parsed.info.type=kotlin.collections.List<kotlin.Any>", out[1])
    }

    @Test
    fun `Property is mapped to Any? when conflicting types and null where used`() {
        val out = captureOut {
            val res = evalFile("with-conflicting-types-and-null.json.kts")
            assertSucceeded(res)
        }.lines()

        Assert.assertEquals("parsed.info=[null, 42, null, 1337]", out[0])
        Assert.assertEquals("parsed.info.type=kotlin.collections.List<kotlin.Any?>", out[1])
    }

}

private fun assertSucceeded(res: ResultWithDiagnostics<EvaluationResult>) {
    Assert.assertTrue(
        "test failed:\n  ${res.reports.joinToString("\n  ") { it.message + if (it.exception == null) "" else ": ${it.exception}" }}",
        res is ResultWithDiagnostics.Success
    )
}

private fun evalFile(scriptPath: String): ResultWithDiagnostics<EvaluationResult> {
    val scriptFile = File("testData/$scriptPath")
    val scriptDefinition = createJvmCompilationConfigurationFromTemplate<Script>()

    val evaluationEnv = ScriptEvaluationConfiguration {
        jvm {
            baseClassLoader(null)
        }
        constructorArgs(emptyArray<String>())
        enableScriptsInstancesSharing()
    }

    return BasicJvmScriptingHost().eval(scriptFile.toScriptSource(), scriptDefinition, evaluationEnv)
}

private fun captureOut(body: () -> Unit): String {
    val outStream = ByteArrayOutputStream()
    val prevOut = System.out
    System.setOut(PrintStream(outStream))
    try {
        body()
    } finally {
        System.out.flush()
        System.setOut(prevOut)
    }
    return outStream.toString().trim()
}