package com.pillskeeper.activity.pills

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.pillskeeper.R
import com.pillskeeper.datamanager.UserInformation
import kotlinx.android.synthetic.main.activity_text_reader.*

class TextReaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_reader)



        val cameraView = findViewById<SurfaceView>(R.id.surface_view)
        val textView = findViewById<TextView>(R.id.text_view)


        var textRecognizer = TextRecognizer.Builder(applicationContext).build()

        if (!textRecognizer.isOperational) {
            Toast.makeText(applicationContext, "Detector dependencies are not avaiable!", Toast.LENGTH_LONG).show()
        } else {
            val cameraSource = CameraSource.Builder(applicationContext, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build()


            cameraView.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(
                    holder: SurfaceHolder?,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                    //TODO questo metodo non deve fare nulla e va bene così
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    cameraSource.stop()
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    try {

                        cameraSource.start(cameraView.holder)

                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "I permessiii!!!", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })

            textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
                override fun release() {

                }

                override fun receiveDetections(detections: Detector.Detections<TextBlock>?) {
                    var items = detections?.detectedItems
                    if (items?.size() != 0) {
                        textView.post {
                            val stringBuilder = StringBuilder()
                            if (items != null) {
                                for (i in 0 until items.size()) {
                                    val item = items.valueAt(i)
                                    stringBuilder.append(item.value)
                                    stringBuilder.append("\n")
                                }

                                textView.text = formatText(stringBuilder.toString())
                            }
                        }
                    }
                }

            })

            confirm_button.setOnClickListener{
                val it = Intent(this, PillsFormActivity::class.java)
                it.putExtra("pillName", textView.text.toString())
                setResult(Activity.RESULT_OK, it)
                finish()
            }
        }
    }

    private fun formatText(str: String): String{
        var result: String = str
        result.contains('|', false)
        result.replace('|', '\b')

        return result
        /*
        for(c in str){
             when (c) {
                '\\' -> Log.d("TextReader", "prova format")
                '|' -> Log.d("TextReader", "prova format")
                '!' -> Log.d("TextReader", "prova format")
                '"' -> Log.d("TextReader", "prova format")
                '£' -> Log.d("TextReader", "prova format")
                '$' -> Log.d("TextReader", "prova format")
                '%' -> Log.d("TextReader", "prova format")
                '&' -> Log.d("TextReader", "prova format")
                '/' -> Log.d("TextReader", "prova format")
                '(' -> Log.d("TextReader", "prova format")
                ')' -> Log.d("TextReader", "prova format")
                '=' -> Log.d("TextReader", "prova format")
                '?' -> Log.d("TextReader", "prova format")
                '^' -> Log.d("TextReader", "prova format")
                '[' -> Log.d("TextReader", "prova format")
                ']' -> Log.d("TextReader", "prova format")
                '§' -> Log.d("TextReader", "prova format")
                '#' -> Log.d("TextReader", "prova format")
                '_' -> Log.d("TextReader", "prova format")
                '<' -> Log.d("TextReader", "prova format")
                '>' -> Log.d("TextReader", "prova format")
                '°' -> Log.d("TextReader", "prova format")
                'ç' -> str.
                 else ->
            }
        }

         */
    }
}