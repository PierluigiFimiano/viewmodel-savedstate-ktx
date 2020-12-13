@file:Suppress("unused")

package com.pierluigifimiano.viewmodelsavedstatektx

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.properties.ReadWriteProperty

abstract class SavedStateViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    fun <T> liveData(key: String): MutableLiveData<T> = savedStateHandle.getLiveData(key)

    fun <T> liveData(key: String, initialValue: T): MutableLiveData<T> =
        savedStateHandle.getLiveData(key, initialValue)

    fun <T> savedState(key: String): ReadWriteProperty<Any?, T?> = savedStateHandle.savedState(key)

    fun <T : Any> savedState(key: String, defaultValue: T): ReadWriteProperty<Any?, T> =
        savedStateHandle.savedState(key, defaultValue)

    fun <T : Any> savedStateNotNull(key: String): ReadWriteProperty<Any?, T> =
        savedStateHandle.savedStateNotNull(key)

    operator fun <T> set(key: String, value: T): Unit = savedStateHandle.set(key, value)

    operator fun <T> get(key: String): T? = savedStateHandle.get(key)

    fun keys(): Set<String> = savedStateHandle.keys()

    fun contains(key: String): Boolean = savedStateHandle.contains(key)
}