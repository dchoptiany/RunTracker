package com.example.gallery

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.common.util.concurrent.ListenableFuture
import android.provider.MediaStore
import android.view.Surface
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.runtracker.R
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Camera : AppCompatActivity() {


    private lateinit var cameraExecutor: ExecutorService
    private lateinit var savedFilePath: String
    lateinit var addButton: Button
    private lateinit var cameraProvider: ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView: PreviewView


    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        savedFilePath =
            getOutputDirectory().absolutePath + File.separator + "${System.currentTimeMillis()}.jpg"
        cameraExecutor = Executors.newSingleThreadExecutor()
        addButton = findViewById(R.id.add)
        previewView = findViewById(R.id.camera)
        cameraProvider = ProcessCameraProvider.getInstance(this)
        requestPermission()
    }

    private fun requestPermission() {
        requestCameraPermissionIfMissing { granted ->
            if (granted) {
                startCamera()
            }
        }
    }

    private fun startCamera() {
        cameraProvider.addListener(Runnable {
            val camera = cameraProvider.get()
            bindPreview(camera)
            val imageCapture = ImageCapture.Builder().setTargetRotation(Surface.ROTATION_0).build()
            runOnUiThread {
                camera.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, imageCapture)
                addButton.setOnClickListener {
                    onClick(imageCapture)
                }
            }
        }, ContextCompat.getMainExecutor(this))
    }

    fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)
        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
    }

    fun onClick(imageCapture: ImageCapture) {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(File(savedFilePath)).build()
        imageCapture.takePicture(outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    Toast.makeText(
                        applicationContext,
                        "Error with initializing camera.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: Uri.fromFile(File(savedFilePath))
                    cameraExecutor.execute {
                        saveImage(savedUri)
                        runOnUiThread {
                            val newIntent = Intent()
                            newIntent.putExtra("imageUri", savedUri.toString())
                            setResult(Activity.RESULT_OK, newIntent)
                            finish()
                        }
                    }
                }
            })
    }


    private fun getOutputDirectory(): File {
        val mediaDirs = externalMediaDirs
        val mediaDir = mediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }


    private fun saveImage(uri: Uri) {
        val bitmap =
            BitmapFactory.decodeStream(applicationContext.contentResolver.openInputStream(uri))

        val directory = ContextWrapper(this).getDir("imageDir", Context.MODE_PRIVATE)
        val fileName = "${System.currentTimeMillis()}.jpg"
        val myImageFile = File(directory, fileName)

        FileOutputStream(myImageFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
        }
    }


    private fun requestCameraPermissionIfMissing(onResult: ((Boolean) -> Unit)) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onResult(true)
        } else {
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                onResult(isGranted)
            }
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}