package net.lumamc.jobsaddons.util

object ClassUtil {

    fun exists(className: String): Boolean {
        return try {
            Class.forName(className)
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }
}