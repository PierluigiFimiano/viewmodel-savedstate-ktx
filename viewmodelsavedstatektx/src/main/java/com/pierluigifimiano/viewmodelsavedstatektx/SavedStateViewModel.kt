@file:Suppress("unused", "MemberVisibilityCanBePrivate", "SameParameterValue")

package com.pierluigifimiano.viewmodelsavedstatektx

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.properties.ReadWriteProperty

/**
 * A subclass of [ViewModel] with an instance of [SavedStateHandle]. This class provides
 * delegate methods for the [savedStateHandle].
 *
 * @param savedStateHandle
 */
abstract class SavedStateViewModel(protected val savedStateHandle: SavedStateHandle) : ViewModel() {

    /**
     * Wrapper function for the [SavedStateHandle.getLiveData] function.
     *
     * @param key
     * @see SavedStateHandle.getLiveData
     */
    protected fun <T> getLiveData(key: String): MutableLiveData<T> =
        savedStateHandle.getLiveData(key)

    /**
     * Wrapper function for the [SavedStateHandle.getLiveData] function.
     *
     * @param key
     * @param initialValue
     * @see SavedStateHandle.getLiveData
     */
    protected fun <T> getLiveData(key: String, initialValue: T): MutableLiveData<T> =
        savedStateHandle.getLiveData(key, initialValue)

    /**
     * Wrapper function for the [SavedStateHandle.property] function.
     *
     * @see SavedStateHandle.property
     * @param key
     */
    protected fun <T> property(key: String? = null): ReadWriteProperty<Any?, T?> =
        savedStateHandle.property(key)

    /**
     * Wrapper function for the [SavedStateHandle.requireProperty] function.
     *
     * @see SavedStateHandle.requireProperty
     * @param key
     * @param defaultValue
     */
    protected fun <T : Any> requireProperty(
        key: String? = null,
        defaultValue: T? = null
    ): ReadWriteProperty<Any?, T> = savedStateHandle.requireProperty(key, defaultValue)

    /**
     * Wrapper function for the [SavedStateHandle.set] function.
     *
     * @see SavedStateHandle.set
     * @param key
     * @param value
     */
    protected operator fun <T> set(key: String, value: T): Unit = savedStateHandle.set(key, value)

    /**
     * Wrapper function for the [SavedStateHandle.get] function.
     *
     * @see SavedStateHandle.get
     * @param key
     */
    protected operator fun <T> get(key: String): T? = savedStateHandle.get(key)

    /**
     * Wrapper function for the [SavedStateHandle.keys] function.
     *
     * @see SavedStateHandle.keys
     */
    protected fun keys(): Set<String> = savedStateHandle.keys()

    /**
     * Wrapper function for the [SavedStateHandle.contains] function.
     *
     * @see SavedStateHandle.contains
     * @param key
     */
    protected fun contains(key: String): Boolean = savedStateHandle.contains(key)
}