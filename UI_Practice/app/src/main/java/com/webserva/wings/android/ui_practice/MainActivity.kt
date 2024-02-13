package com.webserva.wings.android.ui_practice

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))  //ツールバー呼び出し

        val lvMenu = findViewById<ListView>(R.id.lvItems)
        lvMenu.setBackgroundResource(R.drawable.list_item)


        val menuList: MutableList<MutableMap<String, String>> = mutableListOf()

        var product1 :String = "唐揚げ定食"
        var product2 :String = "ハンバーグ定食"

        var priceArray = arrayOf("800円","850円")

        var menu = mutableMapOf("name" to product1, "price" to priceArray[0])
        menuList.add(menu)

        menu = mutableMapOf("name" to product2,"price" to priceArray[1])
        menuList.add(menu)

        val from = arrayOf("name","price")
        val to = intArrayOf(android.R.id.text1,android.R.id.text2)
        val adapter = SimpleAdapter(this@MainActivity,menuList,android.R.layout.simple_list_item_2,from,to)
        lvMenu.adapter = adapter

        lvMenu.onItemClickListener = ListItemClickListener()
    }

    /*  リストのボタンが押されたとき呼び出される関数 */
    private inner class ListItemClickListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long){
            val item = parent.getItemAtPosition(position) as MutableMap<String,String>

            val menuName = item["name"]
            val menuPrice = item["price"]

            val intent2MenuThanks = Intent(this@MainActivity,Scanner::class.java)

            intent2MenuThanks.putExtra("menuName",menuName)
            intent2MenuThanks.putExtra("menuPrice",menuPrice)

            startActivity(intent2MenuThanks)

        }
    }

}