package com.dicoding.sub1storyapp.camera

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dicoding.sub1storyapp.R.string
import com.dicoding.sub1storyapp.databinding.ActivityCameraBinding
import com.dicoding.sub1storyapp.utils.ValManager.CAMERAX_SHOW
import com.dicoding.sub1storyapp.utils.ValManager.KEY_IS_BACK_CAM
import com.dicoding.sub1storyapp.utils.ValManager.KEY_PICT
import com.dicoding.sub1storyapp.utils.extension.showToast
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private var _activityCameraBinding: ActivityCameraBinding? = null
    private val binding get() = _activityCameraBinding!!
    private lateinit var executor: ExecutorService
    private var selector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var capture: ImageCapture? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityCameraBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(_activityCameraBinding?.root)
        initExecutor()
        initAct()
    }

    override fun onResume() {
        super.onResume()
        beginCam()
    }
    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }

    private fun initExecutor() {
        executor = Executors.newSingleThreadExecutor()
    }

    private fun initAct() {
        binding.apply {
            capture.setOnClickListener {
                capture()
            }
        }
    }

    private fun capture() {
        val imgCapture = capture ?: return
        val imgFile = com.dicoding.sub1storyapp.utils.createFile(application)
        val option = ImageCapture.OutputFileOptions.Builder(imgFile).build()
        imgCapture.takePicture(
            option,
            ContextCompat.getMainExecutor(this),
            object: ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra(KEY_PICT, imgFile)
                    intent.putExtra(KEY_IS_BACK_CAM, selector == CameraSelector.DEFAULT_BACK_CAMERA)
                    setResult(CAMERAX_SHOW, intent)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    showToast(getString(string.failed_capture))
                    Timber.e(exception.message.toString())
                }
            }
        )
    }

    private fun beginCam() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.frameCam.surfaceProvider)
                }
            capture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    selector,
                    preview,
                    capture
                )
            } catch (ex: Exception) {
                Toast.makeText(this, "Ada kesalahan pada Kamera", Toast.LENGTH_SHORT).show()
                Timber.e(ex.message)
            }
        }, ContextCompat.getMainExecutor(this))
    }
}