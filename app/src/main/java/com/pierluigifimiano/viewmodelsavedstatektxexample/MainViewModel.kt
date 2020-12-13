package com.pierluigifimiano.viewmodelsavedstatektxexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.pierluigifimiano.viewmodelsavedstatektx.SavedStateViewModel

const val FIRST_VALUE_KEY = "firstValue"

class MainViewModel(savedStateHandle: SavedStateHandle) : SavedStateViewModel(savedStateHandle) {

    private val _myValue: MutableLiveData<Int> = getLiveData("myValue")
    val myValue: LiveData<Int> = _myValue

    private var isFirstTime: Boolean by savedStateNotNull("isFirstTime", true)

    private var firstValue: Int by savedStateNotNull(FIRST_VALUE_KEY)

    private var nullable: Int? by savedState("nullable")

    init {
        if (isFirstTime) {
            _myValue.value = firstValue
            isFirstTime = false
        }
        if (nullable == null) {
            nullable = 1
        }
    }

    fun increase() {
        val value: Int = this["myValue"]!!
        this["myValue"] = value + 1
    }

    fun decrease() {
        val value: Int = this["myValue"]!!
        this["myValue"] = value - 1
    }
}