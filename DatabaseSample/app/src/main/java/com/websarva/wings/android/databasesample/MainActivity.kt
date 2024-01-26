package com.websarva.wings.android.databasesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var _cocktailID = -1
    private var _cocktailName = ""

    private val _helper = DatabaseHelper(this@MainActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lvCocktail = findViewById<ListView>(R.id.lvCocktail)
        lvCocktail.onItemClickListener = ListItemClickListener()
    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

    fun onSaveButtonClick(view: View){
        val etNote = findViewById<EditText>(R.id.etNote)
        val note = etNote.text.toString()
        val db = _helper.writableDatabase

        val sqlDelete = "DELETE FROM cocktailmemos WHERE _id = ?"
        Log.d("MainActivity", "1");

        var stmt = db.compileStatement(sqlDelete)

        stmt.bindLong(1,_cocktailID.toLong())
        stmt.executeUpdateDelete()

        val sqlInsert = "INSERT INTO cocktailmemos (_id, name, note) VALUES (?, ?, ?)"
        Log.d("MainActivity", "2");

        stmt = db.compileStatement(sqlInsert)

        stmt.bindLong(1, _cocktailID.toLong())
        stmt.bindString(2, _cocktailName)
        stmt.bindString(3, note)
        stmt.executeInsert()

        etNote.setText("")

        val tvCocktailName = findViewById<TextView>(R.id.tvCocktailName)
        tvCocktailName.text = getString(R.string.tv_name)

        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.isEnabled = false
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            _cocktailID = position
            _cocktailName = parent.getItemAtPosition(position) as String

            val tvCocktailName = findViewById<TextView>(R.id.tvCocktailName)
            tvCocktailName.text = _cocktailName

            val btnSave = findViewById<Button>(R.id.btnSave)
            btnSave.isEnabled = true

            val db = _helper.writableDatabase
            val sql = "SELECT * FROM cocktailmemos WHERE _id = ${_cocktailID}"
            Log.d("MainActivity", "3");

            val cursor = db.rawQuery(sql, null)
            var note = ""

            while(cursor.moveToNext()){
                val idxNone = cursor.getColumnIndex("none")
                note = cursor.getString(idxNone)
            }

            val etNote = findViewById<EditText>(R.id.etNote)
            etNote.setText(note)
        }
    }
}