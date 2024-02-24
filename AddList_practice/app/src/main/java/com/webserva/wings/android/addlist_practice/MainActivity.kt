package com.webserva.wings.android.addlist_practice

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val names: ArrayList<String> = arrayListOf(
        "Bellflower", "Bougainvillea", "Cosmos", "Cosmos field",
        "Delphinium", "Flowers", "Lotus", "Spring Flowers"
    )

    private val photos: ArrayList<Int> = arrayListOf(
        R.drawable.ard, R.drawable.bomb,
        R.drawable.fire, R.drawable.kyoto,
        R.drawable.mochi, R.drawable.skull,
        R.drawable.torii, R.drawable.trashcan
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view)

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)

        // use a linear layout manager
        val rLayoutManager: RecyclerView.LayoutManager
                = LinearLayoutManager(this)

        recyclerView.layoutManager = rLayoutManager

        recyclerView.adapter = MyAdapter(photos, names)
    }
}

    /*
     private val dataset = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view)

        recyclerView.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        var i = 0
        while (i < 20) {
            val str = String.format(Locale.US, "Data_0%d", i)
            dataset.add(str)
            i++
        }

        val adapter = MyAdapter(dataset)
        recyclerView.adapter = adapter

        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)

        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val fromPos = viewHolder.bindingAdapterPosition
                    val toPos = target.bindingAdapterPosition
                    adapter.notifyItemMoved(fromPos, toPos)
                    return true // true if moved, false otherwise
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }
            })

        mIth.attachToRecyclerView(recyclerView)
    }

     */

/*
class MainActivity : AppCompatActivity() {

    private val dataset: List<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view)
        recyclerView.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        var i = 0
        while (i < 20) {
            val str = String.format(Locale.US, "Data_0%d", i)
            dataset.add(str)
            i++
        }
        val adapter = MyAdapter(dataset)
        recyclerView.adapter = adapter
        val itemDecoration: ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)
        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val fromPos: Int = viewHolder.getBindingAdapterPosition()
                    val toPos: Int = target.getBindingAdapterPosition()
                    adapter.notifyItemMoved(fromPos, toPos)
                    return true // true if moved, false otherwise
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
            })
        mIth.attachToRecyclerView(recyclerView)
    }
}

 */