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

    private var _isLayoutXLarge = true

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

    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)
        val menuThanksFragment = activity?.findViewById<View>(R.id.menuThanksFrame)
        if(menuThanksFragment == null){
            _isLayoutXLarge = false
        }
    }

    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            val item = parent.getItemAtPosition(position) as MutableMap<String, String>
            val menuName = item["name"]
            val menuPrice = item["price"]

            val bundle = Bundle()
            bundle.putString("menuName", menuName)
            bundle.putString("menuPrice", menuPrice)

            if(_isLayoutXLarge) {
                val transaction = fragmentManager?.beginTransaction()
                val menuThanksFragment = MenuThanksFragment()
                menuThanksFragment.arguments = bundle
                transaction?.replace(R.id.menuThanksFrame, menuThanksFragment)
                transaction?.commit()
            }
            else {
                val intent2MenuThanks = Intent(activity, MenuThanksActivity::class.java)
                intent2MenuThanks.putExtras(bundle)
                startActivity(intent2MenuThanks)
            }
        }
    }

}