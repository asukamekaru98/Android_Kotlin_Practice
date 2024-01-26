package com.websarva.wings.android.fragmentsample

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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

        lvMenu.onItemClickListener = ListItemClickListener()

        return view
    }

    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // タップされた行のデータを取得。SimpleAdapterでは1行分のデータはMutableMap型!
            val item = parent.getItemAtPosition(position) as MutableMap<String, String>
            // 定食名と金額を取得。
            val menuName = item["name"]
            val menuPrice = item["price"]
            // インテントオブジェクトを生成。
            val intent2MenuThanks = Intent(activity, MenuThanksActivity::class.java)
            // 第2画面に送るデータを格納。
            intent2MenuThanks.putExtra("menuName", menuName)
            intent2MenuThanks.putExtra("menuPrice", menuPrice)
//			val intent2MenuThanks = Intent(this@MainActivity, MenuThanksActivity::class.java).apply {
//				putExtra("menuName", menuName)
//				putExtra("menuPrice", menuPrice)
//			}
            // 第2画面の起動。
            startActivity(intent2MenuThanks)
        }
    }
}