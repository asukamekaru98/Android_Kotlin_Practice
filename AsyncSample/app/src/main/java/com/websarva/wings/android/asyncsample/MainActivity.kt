package com.websarva.wings.android.asyncsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.core.os.HandlerCompat
import java.lang.reflect.Executable
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    companion object {
        private const val DEBUG_TAG = "AsyncSample"
        private const val WEATHERINFO_URL = "http://api.openweathermap.org/data/2.5/weather?lang=ja"
        private const val APP_ID = "a4e2725b01eb29e04e727a1c8ced94f1"
    }

    private var _list: MutableList<MutableMap<String, String>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _list = createList()

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

        return list
    }

    @UiThread
    private fun receiveWeatherInfo(urlFull: String){
        val handler = HandlerCompat.createAsync(mainLooper)
        val backgroundReceiver = WeatherInfoBackgroundReceiver()
        val executeServer = Executors.newSingleThreadExecutor()
        executeServer.submit(backgroundReceiver)
    }

    private inner class WeatherInfoBackgroundReceiver(handler: Handler,url:String): Runnable{

        private val _handler = handler
        private val _url = url

        @WorkerThread
        override fun run(){
            val postExecutor = WeatherInfoPostExecuter()
            _handler.post(postExecutor)
        }
    }

    private inner class WeatherInfoPostExecuter(): Runnable{

        @UiThread
        override fun run(){

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