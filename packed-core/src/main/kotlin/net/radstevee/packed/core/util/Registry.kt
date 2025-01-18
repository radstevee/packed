package net.radstevee.packed.core.util

import kotlin.random.Random

/**
 * A basic string to object registry.
 */
public open class Registry<T : Any>(
    /**
     * Whether this registry can be modified.
     */
    private val modifiable: Boolean = false,
) {
    private val entries: MutableList<T> = mutableListOf()
    private val keys: MutableList<String> = mutableListOf()
    private val keyToEntryMap: MutableMap<String, T> = mutableMapOf()
    private val entryToKeyMap: MutableMap<T, String> = mutableMapOf()

    /**
     * The amount of registered entries in this registry.
     */
    public val size: Int get() = entries.size

    /**
     * The default value of this registry.
     */
    public val defaultValue: T? = null

    /**
     * Registers [entry] to the registry under [key].
     * @return the passed [entry].
     */
    public fun register(
        key: String,
        entry: T,
    ): T {
        if (keys.contains(key)) {
            if (modifiable) {
                // remove old entry object
                val oldEntry = this[key]
                entries.remove(oldEntry)
                entryToKeyMap.remove(oldEntry)
            } else {
                throw UnsupportedOperationException("Key $key already registered")
            }
        }

        entries.add(entry)
        keys.add(key)

        keyToEntryMap[key] = entry
        entryToKeyMap[entry] = key

        return entry
    }

    /**
     * @return the value that is assigned [key], or `null` if it is not registered
     */
    public operator fun get(key: String): T? = keyToEntryMap[key]

    /**
     * @return the key assigned to [entry], or `null` if it is not registered
     */
    public fun getKey(entry: T): String? = entryToKeyMap[entry]

    /**
     * @return the value that is assigned to the index [index], or null if one is not present
     */
    public operator fun get(index: Int): T? = entries.getOrNull(index)

    /**
     * @return the index of the entry
     */
    public fun indexOf(entry: T): Int = entries.indexOf(entry)

    public fun collectEntries(): List<T> = entries.toList()

    public fun collectKeys(): List<String> = keys.toList()

    /**
     * Performs [block] on every registered entry.
     */
    public fun forEach(block: (T) -> Unit) {
        entries.forEach(block)
    }

    /**
     * Performs [block] on every registered entry.
     */
    public fun forEachEntry(block: (String, T) -> Unit) {
        keyToEntryMap.forEach(block)
    }

    /**
     * Filters the entries list to the given [predicate].
     * @return a new list
     */
    public fun firstOrNull(predicate: (T) -> Boolean): T? = entries.firstOrNull(predicate)

    /**
     * Returns a random entry of the registry.
     */
    public fun random(
        random: Random = Random,
        filter: ((T) -> Boolean)? = null,
    ): T? =
        (
            if (filter != null) {
                entries.filter(filter)
            } else {
                entries
            }
        ).randomOrNull(random)
}
