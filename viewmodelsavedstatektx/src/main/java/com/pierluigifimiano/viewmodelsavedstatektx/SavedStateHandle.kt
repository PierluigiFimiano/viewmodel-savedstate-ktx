package com.pierluigifimiano.viewmodelsavedstatektx

import androidx.lifecycle.SavedStateHandle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private abstract class SavedStateHandleProperty<T : Any?>(
    protected val savedStateHandle: SavedStateHandle,
    private val key: String?
) : ReadWriteProperty<Any?, T> {

    protected fun getKey(property: KProperty<*>): String {
        // If the given key is null, the property.name is used as key
        return key ?: property.name
    }

    final override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        savedStateHandle.set(getKey(property), value)
    }
}

/**
 * Create a delegate property for the given key which is used
 * to access the [SavedStateHandle]. If the given [key] is null, the property.name is used as key
 * (@see [KProperty.name]).
 *
 * @param key used to get and set the value into the [SavedStateHandle]. If it is null,
 * the [KProperty.name] is used as key
 */
fun <T : Any?> SavedStateHandle.property(key: String? = null): ReadWriteProperty<Any?, T?> =
    object : SavedStateHandleProperty<T?>(this, key) {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return savedStateHandle.get(getKey(property))
        }
    }


/**
 * Create a delegate property for the given key which is used
 * to access the [SavedStateHandle]. If the [SavedStateHandle] doesn't contain the specified key
 * and the [defaultValue] is not null, it is used, else an [IllegalStateException] is thrown.
 * If the given [key] is null, the property.name is used as key (@see [KProperty.name]).
 *
 * @param key used to get and set the value into the [SavedStateHandle]. If it is null,
 * the [KProperty.name] is used as key
 * @param defaultValue if not null, is returned when the [SavedStateHandle]
 * doesn't contain the [key]
 * @throws IllegalStateException is the [SavedStateHandle] doesn't contain the [key]
 * and [defaultValue] is null
 */
@Throws(IllegalStateException::class)
fun <T : Any> SavedStateHandle.requireProperty(
    key: String? = null,
    defaultValue: T? = null
): ReadWriteProperty<Any?, T> = object : SavedStateHandleProperty<T>(this, key) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        var value: T? = savedStateHandle.get(getKey(property))
        if (value == null) {
            // The given key is not present, the default value should be returned.
            if (defaultValue == null) {
                // The default value was not specified, an exception is thrown.
                throw IllegalStateException(
                    "SavedStateHandle doesn't contain the key ${getKey(property)}"
                )
            }
            value = defaultValue
        }
        return value
    }
}