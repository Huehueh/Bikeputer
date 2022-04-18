package eu.kinol.bikeputer

import android.bluetooth.BluetoothDevice
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class TurnSignal(val value: Int) {
    NO(0),
    LEFT(1),
    RIGHT(2),
    BOTH(3);

    companion object {
        fun fromInt(intValue: Int) = values().first{ it.value == intValue }
    }
}

class BikeData : ViewModel() {

    var turnSignal: MutableState<TurnSignal> = mutableStateOf(TurnSignal.NO)
    var velocity: MutableState<Double> = mutableStateOf(0.0)
    var distance: MutableState<Double> = mutableStateOf(0.0)
    var connected: MutableState<Boolean> = mutableStateOf(false)
    var betteryLevel: MutableState<Long> = mutableStateOf(0)

    fun updateTurnSignal(turnSignalInt: Int) {
        turnSignal.value = TurnSignal.fromInt(turnSignalInt)
    }
}