package net.lumamc.jobsaddons.storage

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import net.lumamc.jobsaddons.JobsAddons
import net.lumamc.jobsaddons.util.FileUtil.fromInternalResource
import java.util.UUID
import java.util.concurrent.CompletableFuture

abstract class DataSource {

    companion object {
        val DATA_FOLDER = JobsAddons.INSTANCE.dataPath
        var INSTANCE: DataSource = SQLiteDataSource()
            private set
    }

    private val hikari = HikariDataSource(this.hikariConfig())

    abstract fun hikariConfig(): HikariConfig
    // I'm not going to learn kotlin coroutines just for this project
    abstract fun createTables(): CompletableFuture<Void>
    abstract fun selectExecutedCommands(playerUUID: UUID): CompletableFuture<List<String>>
    abstract fun updateExecutedCommands(playerUUID: UUID, commands: List<String>): CompletableFuture<Void>

    protected fun <T> supplyAsync(supplier: () -> T): CompletableFuture<T> = CompletableFuture.supplyAsync(supplier).exceptionallyAsync { throw RuntimeException("DataSource operation failed", it) }
    protected fun getConnection() = hikari.connection


    fun getStatements(path: String): List<String> {
        val statements: List<String> = "sql/$path".fromInternalResource().split(";")
        val newStatements: MutableList<String> = mutableListOf()
        // re-append the semicolon to each statement
        for (i in statements.indices) {
            newStatements.add(statements[i].trim { it <= ' ' } + ";")
        }
        return newStatements
    }

    fun getStatement(path: String): String {
        return "sql/$path".fromInternalResource()
    }

}