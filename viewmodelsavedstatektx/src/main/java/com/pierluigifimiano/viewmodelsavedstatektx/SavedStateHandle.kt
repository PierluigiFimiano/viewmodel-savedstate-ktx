@file:Suppress("ClassName")

package com.pierluigifimiano.viewmodelsavedstatektx

import androidx.lifecycle.SavedStateHandle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal object UNINITIALIZED_VALUE

/**
 * Implement the property access delegation for a [SavedStateHandle]. The given [key] is used to
 * access the [savedStateHandle]. If the [key] is not present into the [savedStateHandle] the
 * [_initializer] is called in order to generate a default value to be returned. If the given [key]
 * is null, the property name is used as key.
 *
 * @param savedStateHandle reference to the [SavedStateHandle] object
 * @param key used to access the [SavedStateHandle]
 * @param initializer provides a default value when the [SavedStateHandle] doesn't contain
 * the given [key]
 */
internal class SavedStateHandleReadWriteProperty<T>(
    private val savedStateHandle: SavedStateHandle,
    private val key: String? = null,
    initializer: () -> T
) : ReadWriteProperty<Any?, T> {

    private var _initializer: (() -> T)? = initializer
    private var defaultValue: Any? = UNINITIALIZED_VALUE

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        // If the given key is null, the property.name is used as key
        savedStateHandle.set(key ?: property.name, value)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        // If the given key is null, the property.name is used as key
        var value: Any? = savedStateHandle.get(key ?: property.name)
        if (value == null) {
            // The given key is not present, the default value must be returned.
            if (defaultValue === UNINITIALIZED_VALUE) {
                /*
                * The default value is not initialized. First it is initialized
                * using the initializer function and after the reference to the initializer function
                * is cleared.
                */
                defaultValue = _initializer!!()
                _initializer = null
            }
            value = defaultValue
        }
        @Suppress("UNCHECKED_CAST")
        return value as T
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
fun <T> SavedStateHandle.savedState(key: String? = null): ReadWriteProperty<Any?, T?> {
    return SavedStateHandleReadWriteProperty(this, key) { null }
}

/**
 * Create a delegate property for the given key which is used
 * to access the [SavedStateHandle]. If the [SavedStateHandle] doesn't contain the specified key
 * the [defaultValue] is returned if it is not null else an [IllegalStateException] is thrown.
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
fun <T : Any> SavedStateHandle.savedStateNotNull(
    key: String? = null,
    defaultValue: T? = null
): ReadWriteProperty<Any?, T> {
    return SavedStateHandleReadWriteProperty(this, key) {
        defaultValue ?: throw IllegalStateException("SavedStateHandle doesn't contain the key $key")
    }
}