package com.websarva.wings.android.fragmentsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // lvMenu.onItemClickListener = ListItemClickListener()
    }

   /* private inner class ListItemClickListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent:AdapterView<*>, view: View, position: Int, id: Long){
            val item = parent.getItemAtPosition(position) as MutableMap<String,String>

            val menuName = item["name"]
            val menuPrice = item["price"]

            val intent2MenuThanks = Intent(activityResultRegistry,  MenuThanksActivity::class.java)

            intent2MenuThanks.putExtra("menuName",menuName)
            intent2MenuThanks.putExtra("menuPrice",menuPrice)

            startActivity(intent2MenuThanks)

        }
    }*/
}