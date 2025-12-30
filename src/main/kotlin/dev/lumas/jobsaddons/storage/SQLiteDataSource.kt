package dev.lumas.jobsaddons.storage

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zaxxer.hikari.HikariConfig
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.util.UUID
import java.util.concurrent.CompletableFuture

class SQLiteDataSource : DataSource() {

    companion object {
        val LIST_STRING_TYPE_TOKEN: Type = object : TypeToken<MutableList<String>?>() {}.type
        val GSON = Gson()
    }

    override fun hikariConfig(): HikariConfig {
        val file: File = DATA_FOLDER.resolve("jobsaddons.db").toFile()
        try {
            file.getParentFile().mkdirs()
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: IOException) {
            throw RuntimeException("Could not create file or dirs", e)
        }

        return HikariConfig().apply {
            poolName = "JobsAddonsSQLite"
            driverClassName = "org.sqlite.JDBC"
            jdbcUrl = "jdbc:sqlite:${file.path}"
            maximumPoolSize = 10
        }
    }

    override fun createTables(): CompletableFuture<Void> {
        return supplyAsync {
            try {
                getConnection().use { conn ->
                    getStatements("create_tables.sql").forEach {string ->
                        conn.prepareStatement(string).execute()
                    }
                }
            } catch (e: Exception) {
                throw RuntimeException("Could not create tables", e)
            }
        }.thenApply { null }
    }

    override fun selectExecutedCommands(playerUUID: UUID): CompletableFuture<List<String>> {
        return supplyAsync {
            try {
                getConnection().use { conn ->
                    var executedCommands: List<String> = ArrayList()

                    val result = conn.prepareStatement(getStatement("select_executed_commands.sql")).apply {
                        setString(1, playerUUID.toString())
                    }.executeQuery()

                    if (result.next()) {
                        val json: List<String>? = GSON.fromJson(result.getString("executed_commands"), LIST_STRING_TYPE_TOKEN)
                        executedCommands = json ?: executedCommands
                    }
                    return@supplyAsync executedCommands
                }
            } catch (e: Exception) {
                throw RuntimeException("Could not get executed commands", e)
            }
        }
    }

    override fun updateExecutedCommands(playerUUID: UUID, commands: List<String>): CompletableFuture<Void> {
        return supplyAsync {
            try {
                getConnection().use { conn ->
                    val json = GSON.toJson(commands)
                    conn.prepareStatement(getStatement("update_executed_commands.sql")).apply {
                        setString(1, playerUUID.toString())
                        setString(2, json)
                    }.executeUpdate()
                }
            } catch (e: Exception) {
                throw RuntimeException("Could not update executed commands", e)
            }
        }.thenApply { null }
    }
}