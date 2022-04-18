package eu.kinol.bikeputer.data

import android.app.Service
import android.bluetooth.*
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.lang.IllegalArgumentException
import android.bluetooth.BluetoothGattDescriptor
import eu.kinol.bikeputer.values.UUID
import java.util.LinkedList
import java.util.Queue


val TAG: String = "ble_service"

class BluetoothLeService : Service() {
    private var bluetoothGatt: BluetoothGatt? = null
    companion object {
        const val ACTION_GATT_CONNECTED = "eu.kinol.bikeputer.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED =
            "eu.kinol.bikeputer.le.ACTION_GATT_DISCONNECTED"
        const val ACTION_GATT_SERVICES_DISCOVERED =
            "eu.kinol.bikeputer.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_AVAILABLE = "eu.kinol.bikeputer.ACTION_DATA_AVAILABLE"
    }

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(
            gatt: BluetoothGatt?,
            status: Int,
            newState: Int
        ) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "successfully connected to the GATT Server")
                broadcastUpdate(ACTION_GATT_CONNECTED)
                bluetoothGatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "disconnected from the GATT Server")
                broadcastUpdate(ACTION_GATT_DISCONNECTED)
            }
        }

        override fun onServicesDiscovered(
            gatt: BluetoothGatt?,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "service discovered")

                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
                setNotifications(getSupportedGattServices())
            }
            else {
                Log.w(TAG, "onServicesDiscovered received: $status")
            }
        }

        var notificationUuids:Queue<String> = LinkedList(listOf(UUID.MOVEMENT, UUID.TURN_SIGNAL))

        private fun setNotifications(
            gattServices: List<BluetoothGattService?>?
        ) {
            if(gattServices == null) return
            if(notificationUuids.size == 0) return

            gattServices.forEach{ gattService ->
                gattService!!.characteristics.forEach{ gattCharacteristic ->
                    try {
                        if(gattCharacteristic.uuid.toString() == notificationUuids.elementAt(0))
                        {
                            Log.i(TAG, "setting notification for ${gattCharacteristic.uuid.toString()}")
                            setCharacteristicNotification(gattCharacteristic, true)
                            notificationUuids.remove()
                            // tylko jedna na raz
                            return
                        }
                    } catch (i: IndexOutOfBoundsException) {

                    }

                }
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            Log.i(TAG, "onCharacteristicChanged")
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
        }

        fun setCharacteristicNotification(
            characteristic: BluetoothGattCharacteristic,
            enabled: Boolean
        ) {
            bluetoothGatt?.let { gatt ->
                gatt.setCharacteristicNotification(characteristic, enabled)
                val notification_uuid = java.util.UUID.fromString(UUID.NOTIFICATION)
                val descriptor = characteristic.getDescriptor(notification_uuid)
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
            }
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            // continue turning on notifications
            if (gatt != null) {
                setNotifications(gatt.services)
            }
        }
    }

    fun connect(bluetoothAdapter: BluetoothAdapter, address: String): Boolean {
        Log.i(TAG, "connect!!!!!!!!!!!!!!!")
        bluetoothAdapter.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback)
                return true
            } catch (exception: IllegalArgumentException) {
                Log.w(TAG, "Device not found with provided address. Unable to connect.")
                return false
            }
        }
    }

    fun getSupportedGattServices(): List<BluetoothGattService?>? {
        return bluetoothGatt?.services
    }

    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        bluetoothGatt?.let { bleGatt ->
            bleGatt.readCharacteristic(characteristic)
        } ?: run {
            Log.w(TAG, "gatt not initialized")
            return
        }
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService() : BluetoothLeService {
            return this@BluetoothLeService
        }
    }

    private fun broadcastUpdate(action: String) {
        Log.i(TAG, "broadcastUpdate $action")
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    private fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic?) {
        Log.i(TAG, "broadcastUpdate $action")
        val intent = Intent(action)
        intent.putExtra(characteristic!!.uuid.toString(), characteristic.value)
        sendBroadcast(intent)
    }
}
