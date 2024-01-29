package com.websarva.wings.android.mediasample

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private var _player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _player = MediaPlayer()
        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.famipop3}"
        val mediaFileUri = Uri.parse(mediaFileUriStr)
        _player?.let{
            it.setDataSource(this@MainActivity, mediaFileUri)
            it.setOnPreparedListener(PlayerPreparedListener())
            it.setOnCompletionListener(PlayerCompletionListener())
            it.prepareAsync()
        }

    }

    private inner class PlayerPreparedListener:MediaPlayer.OnPreparedListener{
        override fun onPrepared(mp: MediaPlayer?) {
            val btPlay = findViewById<Button>(R.id.btPlay)
            btPlay.isEnabled = true
            val btBack = findViewById<Button>(R.id.btBack)
            btBack.isEnabled = true
            val btForward = findViewById<Button>(R.id.btPlay)
            btForward.isEnabled = true
        }
    }

    private inner class PlayerCompletionListener: MediaPlayer.OnCompletionListener{
        override fun onCompletion(mp: MediaPlayer?) {
            val btPlay = findViewById<Button>(R.id.btPlay)
            btPlay.setText(R.string.bt_play_play)
        }
    }

    fun onPlayButtonClick(view: View){
        _player?.let{
            val btPlay = findViewById<Button>(R.id.btPlay)
            if(it.isPlaying){
                it.pause()
                btPlay.setText(R.string.bt_play_play)
            }else{
                it.start()
                btPlay.setText(R.string.bt_play_pause)
            }
        }
    }

    override fun onDestroy() {

        _player?.let {
            if(it.isPlaying){
                it.stop()
            }
            it.release()
        }
        _player = null;
        super.onDestroy()
    }

    fun onBackButtonClick(view: View){
        _player?.seekTo(0)
    }

    fun onForwardButtonClock(view: View){
        _player?.let{
            val duration = it.duration
            it.seekTo(duration)
            if(!it.isPlaying){
                it.start()
            }
        }
    }
}