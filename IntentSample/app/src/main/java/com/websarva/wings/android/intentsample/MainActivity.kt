package com.websarva.wings.android.intentsample

import android.app.LauncherActivity.ListItem
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lvMenu = findViewById<ListView>(R.id.lvMenu)
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

    private inner class ListItemClickListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent:AdapterView<*>, view: View, position: Int, id: Long){
            val item = parent.getItemAtPosition(position) as MutableMap<String,String>

            val menuName = item["name"]
            val menuPrice = item["price"]

            val intent2MenuThanks = Intent(this@MainActivity,MenuThanksActivity::class.java)

            intent2MenuThanks.putExtra("menuName",menuName)
            intent2MenuThanks.putExtra("menuPrice",menuPrice)

            startActivity(intent2MenuThanks)

        }
    }


}