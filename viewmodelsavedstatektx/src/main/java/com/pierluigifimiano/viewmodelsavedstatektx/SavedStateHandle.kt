package com.pierluigifimiano.viewmodelsavedstatektx

import androidx.lifecycle.SavedStateHandle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Create a delegate property for the given key which is used
 * to access the [SavedStateHandle]. If the given [key] is null, the property.name is used as key
 * (@see [KProperty.name]).
 *
 * @param key used to get and set the value into the [SavedStateHandle]. If it is null,
 * the [KProperty.name] is used as key
 */
fun <T : Any?> SavedStateHandle.property(key: String? = null): ReadWriteProperty<Any?, T?> {
    return object : ReadWriteProperty<Any?, T?> {
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            // If the given key is null, the property.name is used as key
            set(key ?: property.name, value)
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            // If the given key is null, the property.name is used as key
            return get(key ?: property.name)
        }
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
): ReadWriteProperty<Any?, T> {
    return object : ReadWriteProperty<Any?, T> {
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            // If the given key is null, the property.name is used as key
            set(key ?: property.name, value)
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            // If the given key is null, the property.name is used as key
            val k: String = key ?: property.name
            var value: T? = get(k)
            if (value == null) {
                // The given key is not present, the default value must be returned.
                if (defaultValue == null) {
                    // The default value was not specified, an exception is thrown.
                    throw IllegalStateException("SavedStateHandle doesn't contain the key $k")
                }
                value = defaultValue
            }
            return value
        }
    }
}