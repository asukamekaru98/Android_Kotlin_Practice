package com.websarva.wings.android.bluetooth_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import java.util.*
import java.io.IOException
import android.bluetooth.BluetoothSocket
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_BLUETOOTH_CONNECT = 100
        private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        //private const val DEVICE_ADDRESS = "58:11:22:D7:81:C3"  // Python側(PC)のMACアドレスを記入しましょう！
       // private const val DEVICE_ADDRESS = "00:A5:54:C2:D7:96" //デスクトップPC
        private const val DEVICE_ADDRESS = "D8:80:39:F6:01:AF" //プリンタ
    }

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothSocket: BluetoothSocket? = null
    private var isConnected: Boolean = false
    private var timerTask: TimerTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {
            /*  Bluetoothが利用できないとき    */

            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show()
            finish()
            return
        }


        requestBluetoothPermissions()
    }

    private fun requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                this,
                /*Manifest.permission.BLUETOOTH_CONNECT*/
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                //arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                arrayOf(Manifest.permission.BLUETOOTH),
                REQUEST_BLUETOOTH_CONNECT
            )
        } else {
            setupBluetoothConnection()
        }
    }

    private fun setupBluetoothConnection() {
        if (ContextCompat.checkSelfPermission(
                this,
                /*Manifest.permission.BLUETOOTH_CONNECT*/
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
                val device = pairedDevices?.firstOrNull { it.address == DEVICE_ADDRESS }

              //  Log.d("TAG", it.address.toString())
                Log.d("TAG", device.toString())

                device?.let {
                    bluetoothSocket = it.createRfcommSocketToServiceRecord(MY_UUID)
                    timerTask = object : TimerTask() {

                        override fun run() {
                            try {
                                Log.d("TAG", "B")

                                if (bluetoothSocket?.isConnected == false) {
                                    Log.d("TAG", "C")
                                    bluetoothSocket?.connect()
                                    isConnected = true
                                }
                                Log.d("TAG", "D")

                                val FS = 0x1c
                                var btStr = "x1cK21000000039830209181000007"

                                //ここでデバイスに送信をしている
                                //bluetoothSocket?.outputStream?.write("K21000000039830214201000003".toByteArray())
                                bluetoothSocket?.outputStream?.write(btStr.toByteArray(Charsets.UTF_8))

                            } catch (e: IOException) {
                                Log.d("TAG", "E")
                                isConnected = false
                                cancel()
                            }
                        }
                    }
                    Log.d("TAG", "てす")
                    Timer().schedule(timerTask, 0, 1000)
                }
                Log.d("TAG", "まいくてす")
            } catch (e: IOException) {
                /*  Bluetoothソケットを作成できなかった時 */
                Toast.makeText(
                    this,
                    "Could not create Bluetooth socket",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: SecurityException) {
                /*  Bluetoothの許可が降りていない時  */
                Toast.makeText(
                    this,
                    "Bluetooth permission is required",
                    Toast.LENGTH_SHORT
                ).show()
            }catch(e:Exception){
                Toast.makeText(
                    this,
                    "どれでもない",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            /*  Bluetoothの許可が降りていない時    */

            Toast.makeText(
                this,
                "Bluetooth permission is required",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerTask?.cancel()
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            //
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_BLUETOOTH_CONNECT -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setupBluetoothConnection()
                } else {
                    Toast.makeText(
                        this,
                        "Bluetooth permission is required",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}