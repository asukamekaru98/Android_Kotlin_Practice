package com.websarva.wings.android.lifecyclesample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("LifeCycleSample","Main onCreate() called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public override fun onStart(){
        Log.i("LifeCycleSample","Main onStart() called")
        super.onStart()
    }

    public override fun onRestart(){
        Log.i("LifeCycleSample","Main onReStart() called")
        super.onRestart()
    }

    public override fun onPause(){
        Log.i("LifeCycleSample","Main onPause() called")
        super.onPause()
    }

    public override fun onStop(){
        Log.i("LifeCycleSample","Main onStop() called")
        super.onStop()
    }

    public override fun onDestroy(){
        Log.i("LifeCycleSample","Main onDestroy() called")
        super.onDestroy()
    }

    fun onButtonClick(view: View){
        //インデントオブジェクトを用意
        val intent = Intent(this@MainActivity,SubActivity::class.java)
        //アクティビティを実行
        startActivity(intent)
    }
}