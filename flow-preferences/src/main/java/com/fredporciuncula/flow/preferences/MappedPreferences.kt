package com.fredporciuncula.flow.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map

/**
 * Returns the preference parameters after applying the [mapper] to convert the output
 * and [reverse] to convert the input back to original values.
 */
fun <T, R> Preference<T>.map(
  mapper: (T) -> R,
  reverse: (R) -> T
): Preference<R> = MappedPreference(this, mapper, reverse)

/**
 * Return the preference parameter mapped to the values in the given map
 */
fun <T, R> Preference<T>.mapToEntries(entries: Map<T, R>): Preference<R> = map({
  entries[it]
    ?: entries[defaultValue]
    ?: throw IllegalArgumentException("No such value '$it' in entries")
}) {
  entries.entries.associate { (k, v) -> v to k }[it]
    ?: throw IllegalArgumentException("No such key '$it' in entries")
}

/**
 * Return the preference parameter mapped to the values in the given pairs
 */
fun <T, R> Preference<T>.mapToEntries(vararg entries: Pair<T, R>): Preference<R> =
  mapToEntries(entries.toList().associate { it })

internal class MappedPreference<T, R>(
  private val preference: Preference<T>,
  private val mapper: (T) -> R,
  private val reverse: (R) -> T
) : Preference<R> {

  override val key: String get() = preference.key

  override val defaultValue: R get() = mapper(preference.defaultValue)

  override fun get(): R = mapper(preference.get())

  override fun set(value: R) = preference.set(reverse(value))

  override suspend fun setAndCommit(value: R): Boolean = preference.setAndCommit(reverse(value))

  override fun isSet(): Boolean = preference.isSet()

  override fun isNotSet(): Boolean = preference.isNotSet()

  override fun delete() = preference.delete()

  override suspend fun deleteAndCommit(): Boolean = preference.deleteAndCommit()

  override fun asFlow(): Flow<R> = preference.asFlow().map { mapper(it) }

  override fun asCollector() = FlowCollector<R> { value -> preference.asCollector().emit(reverse(value)) }

  override fun asSyncCollector(throwOnFailure: Boolean) = FlowCollector<R> { value ->
    preference.asSyncCollector(throwOnFailure).emit(reverse(value))
  }
}
