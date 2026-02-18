@file:JvmName("SynchronizedExecutors")

import dev.lumas.jobsaddons.JobsAddons
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity

fun Entity.sync(runnable: Runnable): Boolean {
    return this.scheduler.execute(JobsAddons.INSTANCE, runnable, null, 1)
}

fun Location.sync(runnable: Runnable) {
    Bukkit.getRegionScheduler().execute(JobsAddons.INSTANCE, this, runnable)
}

fun Block.sync(runnable: Runnable) {
    Bukkit.getRegionScheduler().execute(JobsAddons.INSTANCE, this.location, runnable)
}

fun Entity.syncTimer(delay: Long, period: Long, runnable: Consumer<ScheduledTask>): ScheduledTask? {
    return this.scheduler.runAtFixedRate(JobsAddons.INSTANCE, runnable, null, delay.coerceAtLeast(1), period)
}

fun Collection<Entity>.syncTimer(delay: Long, period: Long, runnable: Consumer<ScheduledTask>): ScheduledTask? {
    return if (this.isEmpty()) {
        null
    } else {
        this.first().scheduler.runAtFixedRate(JobsAddons.INSTANCE, runnable, null, delay.coerceAtLeast(1), period)
    }
}

fun Location.syncTimer(delay: Long, period: Long, runnable: Consumer<ScheduledTask>): ScheduledTask {
    return Bukkit.getRegionScheduler().runAtFixedRate(JobsAddons.INSTANCE, this, runnable, delay.coerceAtLeast(1), period)
}

fun Entity.syncDelayed(delay: Long, runnable: Consumer<ScheduledTask>): ScheduledTask? {
    return this.scheduler.runDelayed(JobsAddons.INSTANCE, runnable, null, delay)
}

fun Location.syncDelayed(delay: Long, runnable: Consumer<ScheduledTask>): ScheduledTask {
    return Bukkit.getRegionScheduler().runDelayed(JobsAddons.INSTANCE, this, runnable, delay)
}


object Executors {

    private fun Long.asMillis() = this * 50

    fun global(runnable: Consumer<ScheduledTask>): ScheduledTask {
        return Bukkit.getGlobalRegionScheduler().run(JobsAddons.INSTANCE, runnable)
    }

    fun async(runnable: Consumer<ScheduledTask>): ScheduledTask {
        return Bukkit.getAsyncScheduler().runNow(JobsAddons.INSTANCE) { task ->
            try {
                runnable.accept(task)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun asyncTimer(delay: Long, period: Long, runnable: Consumer<ScheduledTask>): ScheduledTask {
        return Bukkit.getAsyncScheduler().runAtFixedRate(JobsAddons.INSTANCE, Consumer { task ->
            try {
                runnable.accept(task)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, delay.asMillis(), period.asMillis(), TimeUnit.MILLISECONDS)
    }

    fun asyncTimer(timeUnit: TimeUnit, delay: Long, period: Long, consumer: Consumer<ScheduledTask>): ScheduledTask {
        return Bukkit.getAsyncScheduler().runAtFixedRate(JobsAddons.INSTANCE, Consumer { task ->
            try {
                consumer.accept(task)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, delay, period, timeUnit)
    }

    fun asyncDelayed(delay: Long, runnable: Consumer<ScheduledTask>): ScheduledTask {
        return Bukkit.getAsyncScheduler().runDelayed(JobsAddons.INSTANCE, Consumer { task ->
            try {
                runnable.accept(task)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, delay.asMillis(), TimeUnit.MILLISECONDS)
    }

    fun asyncDelayed(timeUnit: TimeUnit, delay: Long, consumer: Consumer<ScheduledTask>): ScheduledTask {
        return Bukkit.getAsyncScheduler().runDelayed(JobsAddons.INSTANCE, Consumer { task ->
            try {
                consumer.accept(task)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, delay, timeUnit)
    }

    fun <T> (() -> T).async(): ScheduledTask {
        return async { this() }
    }
}