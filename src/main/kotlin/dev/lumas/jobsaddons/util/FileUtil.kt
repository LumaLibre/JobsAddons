package dev.lumas.jobsaddons.util

import dev.lumas.jobsaddons.JobsAddons
import java.io.IOException
import java.nio.charset.StandardCharsets

object FileUtil {

    fun String.fromInternalResource(): String {
        val path = this
        val loader = JobsAddons::class.java.getClassLoader()
        try {
            loader.getResourceAsStream(path).use { inputStream ->
                requireNotNull(inputStream) { "Resource not found: $path (using classloader: $loader)" }
                return String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to read resource: $path", e)
        }
    }
}