package com.websarva.wings.android.bluetooth_practice

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.websarva.wings.android.bluetooth_practice.Constants.PREF_INPUT_VALUES
import java.io.IOException
import java.util.TimerTask
import java.util.UUID

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_BLUETOOTH_CONNECT = 100
        private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothSocket: BluetoothSocket? = null
    private var isConnected: Boolean = false
    private var timerTask: TimerTask? = null

    private var sEditTextBtText:String = ""
    private var sEditTextSendText:String = ""


    /* onCreate ・・・ アクティビティがシステムで初めて生成されるときに呼ばれる*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etBluetoothAdrs = findViewById<EditText>(R.id.etBluetoothAddress)   //Bluetoothアドレス EditText
        val etSendValue = findViewById<EditText>(R.id.etSendValue)              //送信値 EditText
        val btAdrsBtn = findViewById<Button>(R.id.btSend)                       //"送信"ボタン
        val tvBtStat = findViewById<TextView>(R.id.tvBluetoothStat)             //"送信"ボタン

        //getSharedPreferencesメソッドでSharedPreferencesオブジェクトを取得
        val sharedPref = getSharedPreferences(PREF_INPUT_VALUES, Context.MODE_PRIVATE)

        // getString()を呼び出して保存されている文字列を読み込む
        // まだ保存されていない場合はデフォルトの文字列を返す
        var savedEditTextBtText = sharedPref.getString(Constants.KEY_BT_ADRS, Constants.TXT_DEF_BT_ADRS)
        val savedEditTextSendValText = sharedPref.getString(Constants.KEY_SEND_VAL, Constants.TXT_DEF_BLANK)


        if(savedEditTextBtText == ""){
            Log.d("TAG", "sEditTextBtText is true = $savedEditTextBtText")
            //etBluetoothAdrs.setText(Constants.TXT_DEF_BT_ADRS)    //テキスト設定 (Bluetoothアドレス EditText)
            savedEditTextBtText = Constants.TXT_DEF_BT_ADRS    //テキスト設定 (Bluetoothアドレス EditText)
        }else{
            Log.d("TAG", "sEditTextBtText is false = $savedEditTextBtText")
        }

        Log.d("TAG", "savedEditTextBtText = $savedEditTextBtText")

        etBluetoothAdrs.setText(savedEditTextBtText)    //テキスト設定 (Bluetoothアドレス EditText)
        etBluetoothAdrs.requestFocus()                  // フォーカスを設定

        etSendValue.setText(savedEditTextSendValText)   //テキスト設定 (送信値 EditText)

        btAdrsBtn.setOnClickListener {
            // テキストボックスに入力されている文字列を取得
            sEditTextBtText = etBluetoothAdrs.text.toString()
            sEditTextSendText = etSendValue.text.toString()

            // プリファレンスに書き込む
            sharedPref.edit().putString(Constants.KEY_BT_ADRS, sEditTextBtText).apply()
            sharedPref.edit().putString(Constants.KEY_SEND_VAL, sEditTextSendText).apply()

            requestBluetoothPermissions()//BT発射
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {
            /*  Bluetoothが利用できないとき    */

            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        //requestBluetoothPermissions()
    }

    private fun requestBluetoothPermissions() {

        Log.d("TAG", "requestBluetoothPermissionsに入る")

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

    /*
    private fun setupBluetoothConnection() {





        //Bluetoothの接続許可が与えられているか確認
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
            try {
                // ペアリングされたBluetoothデバイスのリストを取得
                val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

                // 今回指定したBluetoothアドレスを持つデバイスを検索
                val device = pairedDevices?.firstOrNull { it.address == sEditTextBtText }

                device?.let {
                    //ソケット作成
                    bluetoothSocket = it.createRfcommSocketToServiceRecord(MY_UUID)
                    Log.d("TAG", "bluetoothSocket = $bluetoothSocket")

                            try {
                                //ソケット接続がなければ、接続を試す
                                if (bluetoothSocket?.isConnected == false) {
                                    bluetoothSocket?.connect()
                                    isConnected = true
                                }
                                Log.d("TAG", "D")

                                //ここでデバイスに送信をしている
                                bluetoothSocket?.outputStream?.write(sEditTextSendText.toByteArray(Charsets.UTF_8))

                            } catch (e: IOException) {
                                Log.d("TAG", "IOException")

                                isConnected = false
                            }

                }

                    try {
                        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
                        val device = pairedDevices?.firstOrNull { it.address == sEditTextBtText }

                        device?.let {
                            if (bluetoothSocket == null || bluetoothSocket?.isConnected == false) {
                                bluetoothSocket = it.createRfcommSocketToServiceRecord(MY_UUID)
                                bluetoothSocket?.connect()
                                isConnected = true
                            }

                            bluetoothSocket?.outputStream?.write(sEditTextSendText.toByteArray(Charsets.UTF_8))
                        }

                        Log.d("TAG", "Bluetooth connection setup successful")
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
                //その他エラー
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
     */

    private fun setupBluetoothConnection() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
            try {
                val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
                val device = pairedDevices?.firstOrNull { it.address == sEditTextBtText }

                device?.let {
                    if (bluetoothSocket == null || bluetoothSocket?.isConnected == false) {
                        bluetoothSocket = it.createRfcommSocketToServiceRecord(MY_UUID)
                        bluetoothSocket?.connect()
                        isConnected = true
                    }

                    //bluetoothSocket?.outputStream?.write(sEditTextSendText.toByteArray(Charsets.UTF_8))
                    bluetoothSocket?.outputStream?.write(sEditTextSendText.toByteArray())
                }

                Log.d("TAG", "Bluetooth connection setup successful")
            } catch (e: IOException) {
                Toast.makeText(
                    this,
                    "Could not establish Bluetooth connection",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("TAG", "IOException: ${e.message}")
            } catch (e: SecurityException) {
                Toast.makeText(
                    this,
                    "Bluetooth permission is required",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("TAG", "SecurityException: ${e.message}")
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "An unexpected error occurred",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("TAG", "Unexpected error: ${e.message}")
            }
        } else {
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