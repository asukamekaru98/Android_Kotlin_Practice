package com.websarva.wings.android.asyncsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.core.os.HandlerCompat
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.lang.reflect.Executable
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    companion object {
        private const val DEBUG_TAG = "AsyncSample"
        private const val WEATHERINFO_URL = "https://api.openweathermap.org/data/2.5/weather?lang=ja"
        private const val APP_ID = "a4e2725b01eb29e04e727a1c8ced94f1"
    }

    private var _list: MutableList<MutableMap<String, String>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _list = createList()

        Log.d("MainActivity", "mogumogu")

        val lvCityList = findViewById<ListView>(R.id.lvCityList)
        val from = arrayOf("name")
        val to = intArrayOf(android.R.id.text1)
        val adapter = SimpleAdapter(this@MainActivity, _list, android.R.layout.simple_list_item_1, from, to)
        lvCityList.adapter = adapter
        lvCityList.onItemClickListener = ListItemClickListener()
    }

    private fun createList(): MutableList<MutableMap<String, String>>{
        var list: MutableList<MutableMap<String, String>> = mutableListOf()

        var city = mutableMapOf("name" to "大阪","q" to "Osaka")
        list.add(city)

        city = mutableMapOf("name" to "神戸","q" to "Kobe")
        list.add(city)

        city = mutableMapOf("name" to "東京","q" to "Tokyo")
        list.add(city)

        city = mutableMapOf("name" to "沖縄","q" to "Okinawa")
        list.add(city)

        city = mutableMapOf("name" to "新潟","q" to "Niigata")
        list.add(city)
        Log.d("MainActivity", "piyopiyo")
        return list
    }

    @UiThread
    private fun receiveWeatherInfo(urlFull: String){

        Log.d("MainActivity", "うみゃみゃ")

        val handler = HandlerCompat.createAsync(mainLooper)
        Log.d("MainActivity", "1")
        val backgroundReceiver = WeatherInfoBackgroundReceiver(handler, urlFull)
        Log.d("MainActivity", "2")
        val executeServer = Executors.newSingleThreadExecutor()
        Log.d("MainActivity", "3")
        executeServer.submit(backgroundReceiver)
        Log.d("MainActivity", "4")
    }

    private inner class WeatherInfoBackgroundReceiver(handler: Handler,url:String): Runnable{

        private val _handler = handler
        private val _url = url

        @WorkerThread
        override fun run(){
            var result = ""
            val url = URL(_url)
            val con = url.openConnection() as? HttpURLConnection

            Log.d("MainActivity", _url)
            if(con == null){
                Log.d("MainActivity", "con is null")
            }else{
                Log.d("MainActivity", "con isnot null")
            }




            con?.let {
                Log.d("MainActivity", "入った")
                try {
                    Log.d("MainActivity", "try 入った")

                    it.connectTimeout = 1000
                    it.readTimeout = 1000
                    it.requestMethod = "GET"

                    Log.d("MainActivity", "connect前")
                    it.connect()

                    Log.d("MainActivity", "6")
                    val stream = it.inputStream
                    Log.d("MainActivity", "7")
                    result = is2String(stream)
                    Log.d("MainActivity", "8")
                    stream.close()
                    Log.d("MainActivity", "9")

                } catch (ex: SocketTimeoutException) {
                    Log.w("MainActivity", "通信タイムアウト", ex)
                }
                Log.d("MainActivity", "10")
                it.disconnect()
            }
            Log.d("MainActivity", "うびゃびゃ")
            val postExecutor = WeatherInfoPostExecuter(result)
            _handler.post(postExecutor)
        }

        private fun is2String(stream: InputStream): String{
            val sb = StringBuilder()
            val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
            var line = reader.readLine()
            while(line != null){
                sb.append(line)
                line = reader.readLine()
            }
            reader.close()
            return sb.toString()
        }
    }

    private inner class WeatherInfoPostExecuter(result: String): Runnable{

        private val _result = result

        @UiThread
        //インターネットの世界へGO!!!!!!!!!!!!
        override fun run() {
            Log.d("MainActivity", "hoge")
            val rootJSON = JSONObject(_result)
            val cityName = rootJSON.getString("name")
            val coordJSON = rootJSON.getJSONObject("coord")
            val latitude = coordJSON.getString("lat")
            val longitude = coordJSON.getString("lon")
            val weatherJSONArray = rootJSON.getJSONArray("weather")
            val weatherJSON = weatherJSONArray.getJSONObject(0)
            val weather = weatherJSON.getString("description")
            val telop = "${cityName}の天気"
            val desc = "現在は${weather}です。\n経度は${latitude}度で経度は${longitude}度です"
            val tvWeatherTelop = findViewById<TextView>(R.id.tvWeatherTelop)
            val tvWeatherDesc = findViewById<TextView>(R.id.tvWeatherDesc)

            tvWeatherTelop.text = telop
            tvWeatherDesc.text = desc
        }
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val item = _list.get(position)
            val q = item.get("q")
            q?.let{
                val urlFull = "$WEATHERINFO_URL&q=$q&appid=$APP_ID"
                receiveWeatherInfo(urlFull)
            }
        }
    }
}