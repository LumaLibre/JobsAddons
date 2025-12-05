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

    @JvmStatic
    fun <E : Enum<E>> enumValueOfOrNull(enumClass: Class<E>, name: String): E? {
        return try {
            java.lang.Enum.valueOf(enumClass, name)
        } catch (ignored: IllegalArgumentException) {
            null
        }
    }
}