package com.dicoding.sub1storyapp.story

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dicoding.sub1storyapp.R.string
import com.dicoding.sub1storyapp.camera.CameraActivity
import com.dicoding.sub1storyapp.data.remote.ApiResponse
import com.dicoding.sub1storyapp.databinding.ActivityAddStoryBinding
import com.dicoding.sub1storyapp.utils.Session
import com.dicoding.sub1storyapp.utils.ValManager.CAMERAX_SHOW
import com.dicoding.sub1storyapp.utils.ValManager.KEY_PICT
import com.dicoding.sub1storyapp.utils.ValManager.REQUEST_PERMISSIONS
import com.dicoding.sub1storyapp.utils.extension.gone
import com.dicoding.sub1storyapp.utils.extension.show
import com.dicoding.sub1storyapp.utils.extension.showOKDialog
import com.dicoding.sub1storyapp.utils.extension.showToast
import com.dicoding.sub1storyapp.utils.reduceImage
import com.dicoding.sub1storyapp.utils.toFile
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


@Suppress("DEPRECATION")
@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    private val storyVm: StoryViewModel by viewModels()
    private var _activityAddStoryBinding: ActivityAddStoryBinding? = null
    private val binding get() = _activityAddStoryBinding!!
    private var uploadImg: File? = null
    private var token: String? = null
    private lateinit var pref: Session

    companion object {
        fun begin(context: Context) {
            val intent = Intent(context, AddStoryActivity::class.java)
            context.startActivity(intent)
        }
        private val CAM_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityAddStoryBinding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(_activityAddStoryBinding?.root)

        pref = Session(this)
        token = pref.getToken

        if (!permissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                CAM_PERMISSIONS,
                REQUEST_PERMISSIONS
            )
        }
        initUI()
        initAct()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!permissionsGranted()) {
            showToast(getString(string.not_granted))
        }
    }

    private fun permissionsGranted() = CAM_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun initUI() {
        title = getString(string.add_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initAct() {
        binding.btnOpenCam.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intentCamera.launch(intent)
        }
        binding.btnOpenGallery.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, getString(string.pick_image))
            intentGallery.launch(chooser)
        }
        binding.btnUpload.setOnClickListener {
            uploadImg()
        }
    }

    private val intentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_SHOW) {
            val file = it?.data?.getSerializableExtra(KEY_PICT) as File

            uploadImg = file

            val result = BitmapFactory.decodeFile(file.path)

            binding.imgPrev.setImageBitmap(result)
        }
    }

    private val intentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val file = toFile(selectedImg, this)

            uploadImg = file
            binding.imgPrev.setImageURI(selectedImg)
        }
    }

    private fun uploadImg() {
        if (uploadImg != null) {
            val file = reduceImage(uploadImg as File)
            val description = binding.edtStoryDesc.text
            if (description.isBlank()) {
                binding.edtStoryDesc.requestFocus()
                binding.edtStoryDesc.error = getString(string.desc_empty)
            } else {
                val descMediaTyped = description.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                storyVm.addStory("Bearer $token", imageMultipart, descMediaTyped).observe(this) { response ->
                    when(response) {
                        is ApiResponse.Loading -> {
                            isLoading(true)
                        }
                        is ApiResponse.Success -> {
                            isLoading(false)
                            showToast(getString(string.img_upload))
                            finish()
                        }
                        is ApiResponse.Error -> {
                            isLoading(false)
                            showOKDialog(getString(string.img_info), response.errorMessage)
                        }
                        else -> {
                            isLoading(false)
                            showToast(getString(string.statement_error))
                        }
                    }
                }
            }
        } else {
            showOKDialog(getString(string.title_message), getString(string.img_chose))
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
        if (isLoading) binding.bgDim.show() else binding.bgDim.gone()
        binding.apply {
            btnUpload.isClickable = !isLoading
            btnUpload.isEnabled = !isLoading
            btnOpenGallery.isClickable = !isLoading
            btnOpenGallery.isEnabled = !isLoading
            btnOpenCam.isClickable = !isLoading
            btnOpenCam.isEnabled = !isLoading
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}