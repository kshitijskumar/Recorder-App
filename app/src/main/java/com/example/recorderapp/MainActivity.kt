package com.example.recorderapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //private val output by lazy {Environment.getExternalStorageDirectory().absolutePath + "/${listOf(1,2,3).random()}recording.mp3"}

    private val mediaRecorder by lazy { MediaRecorder() }
    private var state: Boolean = false
    private var recordingStopped = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()
        //configureMediaRecorder()

        btnStartRecording.setOnClickListener {
            startRecording()
        }

        btnStopRecording.setOnClickListener {
            stopRecording()
        }

        btnPauseRecording.setOnClickListener {
            pauseRecording()
        }
    }

    private fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            val permissions = arrayOf(
                Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permissions, 0)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startRecording(){
        try {
            configureMediaRecorder()
            mediaRecorder.prepare()
            mediaRecorder.start()
            state = true
            tvStatus.text = "Recording..."
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            Log.d("MainActivity", "Start recording catch ${e.message}")
        }
    }

    private fun stopRecording(){
        if (state){
            mediaRecorder.stop()
            mediaRecorder.release()
            state = false
            tvStatus.text = "Recorder"
            Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "You're not recording", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun pauseRecording(){
        if (state){
            if (!recordingStopped){
                Toast.makeText(this, "Recording paused", Toast.LENGTH_SHORT).show()
                mediaRecorder.pause()
                btnPauseRecording.text = "Resume"
                recordingStopped = true
            }else{
                resumeRecording()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun resumeRecording(){
        Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show()
        mediaRecorder.resume()
        btnPauseRecording.text = "Pause"
    }

    private fun configureMediaRecorder(){
        val output = Environment.getExternalStorageDirectory().absolutePath + "/${listOf(1,2,3).random()}recording.mp3"
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setOutputFile(output)
    }
}