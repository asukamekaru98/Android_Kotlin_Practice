package com.websarva.wings.android.servicesample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.provider.MediaStore.Audio.Media
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class SoundManageSevice : Service() {

    companion object {
        private const val CHANNEL_ID = "soundmanagerservice_notification_channel"
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private var _player: MediaPlayer? = null

    override fun onCreate() {
        _player = MediaPlayer()
        val name = getString(R.string.notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.saya}"

        val mediaFileUrl = Uri.parse(mediaFileUriStr)
        _player?.let{
            it.setDataSource(this@SoundManageSevice, mediaFileUrl)
            it.setOnPreparedListener(PlayerPreparedListener())
            it.setOnCompletionListener(PlayerCompletionListener())
            it.prepareAsync()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        _player?.let{
            if(it.isPlaying){
                it.stop()
            }
            it.release()
        }
        _player = null
    }

    private inner class PlayerPreparedListener : MediaPlayer.OnPreparedListener{
        override fun onPrepared(mp: MediaPlayer) {
            mp.start()
            val builder = NotificationCompat.Builder(this@SoundManageSevice, CHANNEL_ID)
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)
            builder.setContentTitle(getString(R.string.msg_notification_title_start))
            builder.setContentText(getString(R.string.msg_notification_text_start))
            val intent = Intent(this@SoundManageSevice,MainActivity::class.java)
            intent.putExtra("formNotification",true)
            val stopServiceIntent = PendingIntent.getActivity(this@SoundManageSevice,0,intent,PendingIntent.FLAG_CANCEL_CURRENT)
            builder.setContentIntent(stopServiceIntent)
            builder.setAutoCancel(true)
            val notification = builder.build()
            startForeground(200, notification);
        }
    }

    private inner class PlayerCompletionListener : MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer) {
            val builder = NotificationCompat.Builder(this@SoundManageSevice, CHANNEL_ID)
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)
            builder.setContentTitle(getString(R.string.msg_notification_title_finish))
            builder.setContentText(getString(R.string.msg_notification_text_finish))
            val notification = builder.build()
            val manager = NotificationManagerCompat.from(this@SoundManageSevice)
            manager.notify(100, notification)
            stopSelf()
        }
    }

}