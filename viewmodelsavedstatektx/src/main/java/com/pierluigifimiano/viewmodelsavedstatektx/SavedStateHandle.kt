@file:Suppress("ClassName")

package com.pierluigifimiano.viewmodelsavedstatektx

import androidx.lifecycle.SavedStateHandle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal object UNINITIALIZED_VALUE

internal class SavedStateHandleReadWriteProperty<T>(
    private val savedStateHandle: SavedStateHandle,
    private val key: String,
    initializer: () -> T
) : ReadWriteProperty<Any?, T> {

    private var _initializer: (() -> T)? = initializer
    private var defaultValue: Any? = UNINITIALIZED_VALUE

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        savedStateHandle.set(key, value)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val value: T? = savedStateHandle.get(key)
        if (value == null) {
            if (defaultValue === UNINITIALIZED_VALUE) {
                defaultValue = _initializer!!()
                _initializer = null
            }
            @Suppress("UNCHECKED_CAST")
            return defaultValue as T
        }
        return value
    }
}

fun <T> SavedStateHandle.savedState(key: String): ReadWriteProperty<Any?, T?> {
    return SavedStateHandleReadWriteProperty(this, key) { null }
}

fun <T : Any> SavedStateHandle.savedState(
    key: String,
    defaultValue: T
): ReadWriteProperty<Any?, T> {
    return SavedStateHandleReadWriteProperty(this, key) { defaultValue }
}

fun <T : Any> SavedStateHandle.savedStateNotNull(key: String): ReadWriteProperty<Any?, T> {
    return SavedStateHandleReadWriteProperty(this, key) {
        throw IllegalStateException("SavedStateHandle doesn't contain the key $key")
    }
}