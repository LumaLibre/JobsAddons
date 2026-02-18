package dev.lumas.jobsaddons.util

import dev.lumas.jobsaddons.JobsAddons
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

enum class Potions(vararg val types: PotionEffectType) {

    ABSORPTION(PotionEffectType.ABSORPTION),
    FIRE_RESISTANCE(PotionEffectType.FIRE_RESISTANCE),
    NIGHT_VISION(PotionEffectType.NIGHT_VISION),
    STRENGTH(PotionEffectType.STRENGTH),
    HASTE(PotionEffectType.HASTE),
    LUCK(PotionEffectType.LUCK),
    SPEED(PotionEffectType.SPEED),
    REGENERATION(PotionEffectType.REGENERATION),
    JUMP_BOOST(PotionEffectType.JUMP_BOOST),
    RESISTANCE(PotionEffectType.RESISTANCE),
    DOLPHINS_GRACE(PotionEffectType.DOLPHINS_GRACE),
    TRIAD(PotionEffectType.RESISTANCE, PotionEffectType.ABSORPTION, PotionEffectType.REGENERATION);

    val key = NamespacedKey(JobsAddons.INSTANCE, name.lowercase())
    val effects: List<PotionEffect> = types.map {
        PotionEffect(it, 600, 0, true, false, true)
    }


    fun hasKey(player: Player) = player.persistentDataContainer.has(key, PersistentDataType.BOOLEAN)
    fun setKey(player: Player) = player.persistentDataContainer.set(key, PersistentDataType.BOOLEAN, true)
    fun removeKey(player: Player) = player.persistentDataContainer.remove(key)
    fun doEffect(player: Player) = effects.forEach { player.addPotionEffect(it) }
    fun undoEffect(player: Player) = effects.forEach { player.removePotionEffect(it.type) }
}