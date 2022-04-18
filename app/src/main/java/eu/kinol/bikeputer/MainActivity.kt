
package eu.kinol.bikeputer

import android.Manifest
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.*
import android.content.pm.PackageManager
import android.os.*
import androidx.activity.ComponentActivity
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavHostController
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import eu.kinol.bikeputer.data.BleBroadcastReceiver
import eu.kinol.bikeputer.data.BluetoothLeService
import eu.kinol.bikeputer.data.TAG
import eu.kinol.bikeputer.values.Constants


class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }
    private val scanHandler = Handler(Looper.getMainLooper())
    private val SCAN_PERIOD: Long = 100000
    private var scanning = false
    private val bikeData = BikeData()
    private val scanSettings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
//    private var bluetoothGatt: BluetoothGatt? = null
    private var gattUpdateReceiver: BleBroadcastReceiver? = null
    private var bluetoothLeService: BluetoothLeService? = null


    private val scanCallback = object: ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            with(result!!.device) {
                Log.i(TAG, this.name ?: "no name")
                if (this.name == Constants.DEVICE_NAME) {
                    stopScan()
                    bluetoothLeService?.connect(bluetoothAdapter, this.address)
                }
            }
        }
    }

    private val mConnection = object : ServiceConnection {
        var bound = false
        // Called when the connection with the service is established
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Log.i(TAG, "on service connected")
            val binder = service as BluetoothLeService.LocalBinder
            bluetoothLeService = binder.getService()
            bound = true
            startBleScan()
        }

        // Called when the connection with the service disconnects unexpectedly
        override fun onServiceDisconnected(className: ComponentName) {
            Log.e(TAG, "onServiceDisconnected")
            bound = false
            startBleScan()
        }
    }


    private fun Context.hasPermission(permissionType: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permissionType) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun Activity.requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }

    private val isLocationPermissionGranted
        get() = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private val isBlePermissionGranted
        get() = hasPermission(Manifest.permission.BLUETOOTH)
    private val ENABLE_BLUETOOTH_REQUEST_CODE = 1
    private val LOCATION_PERMISSION_REQUEST_CODE = 2

    private fun requestLocationPermission() {
        if (isLocationPermissionGranted) {
            return
        }
        requestPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun requestBluetoothPermission() {
        if (isBlePermissionGranted) {
            return
        }
        requestPermission(
            Manifest.permission.BLUETOOTH,
            ENABLE_BLUETOOTH_REQUEST_CODE
        )
    }

    private fun startBleScan() {
        Log.i(TAG, "startBleScan")
        if(!scanning)
        {
            scanHandler.postDelayed({
                scanning = false
                bleScanner.stopScan(scanCallback)
            }, SCAN_PERIOD)
            scanning = true
            bleScanner.startScan(null, scanSettings, scanCallback)
            Log.v("BLE", "ble start")
        }
    }

    private fun stopScan() {
        scanning = false
        bleScanner.stopScan(scanCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(gattUpdateReceiver)
        if (mConnection.bound)
        {
            unbindService(mConnection)
        }
    }


    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        gattUpdateReceiver = BleBroadcastReceiver(bikeData = bikeData)
        val filter = IntentFilter().apply {
            addAction(BluetoothLeService.ACTION_DATA_AVAILABLE)
            addAction(BluetoothLeService.ACTION_GATT_CONNECTED)
            addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED)
            addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)
        }
        registerReceiver(gattUpdateReceiver!!, filter)

//        setContent{
//            Column() {
//                if(!bikeData.connected.value) {
//                    Box(modifier = Modifier
//                        .size(100.dp)
//                        .clip(RectangleShape)
//                        .background(Color.Red))
//                }
//                else {
//                    Box(modifier = Modifier
//                        .size(100.dp)
//                        .clip(RectangleShape)
//                        .background(Color.Green))
//                }
////                TurnSignalWidget(bikeData = bikeData)
////                VelocityProgressBar(bikeData = bikeData)
//            }
//
//
//        }


        setContent{
            navController = rememberNavController()
            ScreenNavigation(
                navController = navController,
                bikeData = bikeData
            )
        }
    }

    override fun onResume() {
        super.onResume()

        // BLE & location
        requestLocationPermission()
        requestBluetoothPermission()

        Log.i(TAG, "AAAAAAAa")

        if (bluetoothAdapter.isEnabled)
        {
            startBleService()

        } else
        {
            promptEnableBluetooth()
        }
    }

    private fun startBleService() {
        Log.i(TAG, "startBleService")
        Intent(this, BluetoothLeService::class.java).also {
            Log.i(TAG, "intent starting")
            bindService(it, mConnection, Context.BIND_AUTO_CREATE)

        }
    }


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            startBleService()
        }
        else
        {
            promptEnableBluetooth()
        }
    }

    private fun promptEnableBluetooth() {
        if(!bluetoothAdapter.isEnabled) {
            val enableBltIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            resultLauncher.launch(enableBltIntent)

        }
    }

}



