package com.websarva.wings.android.fragmentsample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView

class MenuListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_menu_list, container, false)
        val lvMenu = view.findViewById<ListView>(R.id.lvMenu)
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
        val adapter = SimpleAdapter(activity,menuList,android.R.layout.simple_list_item_2,from, to)
        lvMenu.adapter = adapter

        return view
    }


}