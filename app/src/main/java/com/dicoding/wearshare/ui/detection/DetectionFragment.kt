package com.dicoding.wearshare.ui.detection

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dicoding.wearshare.R
import com.dicoding.wearshare.data.api.ApiConfig
import com.dicoding.wearshare.databinding.FragmentDetectionBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class DetectionFragment : Fragment() {

    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.analyze.setOnClickListener { uploadImage("email") }

        return root
    }

    private fun uploadImage(email: String) {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            Log.d("Image Classification File", "showImage: ${imageFile.path}")
            showLoading(true)
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )
            val emailRequestBody = email.toRequestBody("text/plain".toMediaType())
            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService()
                    val response = apiService.uploadImage(emailRequestBody, multipartBody)
                    if (response.isSuccessful) {
                        val successResponse = response.body()
                        successResponse?.result?.let { result ->
                            binding.resultTextView.text = result
                            showToast("Upload successful: $result")
                        } ?: run {
                            showToast("Upload successful but no result received.")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("Upload Image Error", "Error response: $errorBody")
                        showToast("Upload failed: ${response.message()}")
                    }
                    showLoading(false)
                } catch (e: Exception) {
                    Log.e("Upload Image Exception", "Exception during upload", e)
                    showToast("An error occurred during upload.")
                    showLoading(false)
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

