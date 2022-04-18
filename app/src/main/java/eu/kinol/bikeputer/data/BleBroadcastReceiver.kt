package eu.kinol.bikeputer.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import eu.kinol.bikeputer.*
import eu.kinol.bikeputer.values.UUID
import java.nio.ByteBuffer
import java.nio.ByteOrder


class BleBroadcastReceiver(bikeData: BikeData) : BroadcastReceiver() {

    private var turnSignal: TurnSignal by bikeData.turnSignal
    private var velocity by bikeData.velocity
    private var distance by bikeData.distance
    private var connected by bikeData.connected

    fun littleEndianConversion(bytes: ByteArray): Int {
        var result = 0
        for (i in bytes.indices) {
            result = result or (bytes[i].toInt() shl 8 * i)
        }
        return result
    }

    fun byteArrayToDoubles(bytes: ByteArray): ArrayList<Double> {
        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        var a1: Double =  buffer.getDouble(0)
        var a2: Double =  buffer.getDouble(8)

        return arrayListOf(a1, a2)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        with(intent!!) {
            Log.i(TAG, action!!)
            when (action) {
                BluetoothLeService.ACTION_GATT_CONNECTED -> {
                    connected = true
                    Log.i("BLE", "HUEHUEHEUEHUE")
                }
                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
                    connected = false
                }
                BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED -> {
                    //service.setNotifications(getSupportedGattServices())
                }
                BluetoothLeService.ACTION_DATA_AVAILABLE -> {
                    with(intent.extras!!) {
                        if(containsKey(UUID.TURN_SIGNAL))
                        {
                            var byte:ByteArray = get(UUID.TURN_SIGNAL) as ByteArray
                            var sif = littleEndianConversion(byte)
                            turnSignal = TurnSignal.fromInt(sif)
                            Log.i(TAG, "indykator $turnSignal")
                        }
                        if (containsKey(UUID.MOVEMENT))
                        {
                            var byte:ByteArray = get(UUID.MOVEMENT) as ByteArray
                            var values = byteArrayToDoubles(byte)
                            if (values.size == 2) {
                                velocity = values[0]
                                distance = values[1]
                                Log.i(TAG, "velocity $velocity distance $distance")
                            }
                        }
                    }
                }
                else -> {

                }
            }
        }
    }
}