package com.mobilehealthsports.vaccinepass.ui.user_creation

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.ActivityUserCreationBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.presentation.services.messages.ToastRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationService
import com.mobilehealthsports.vaccinepass.util.BaseActivity
import com.mobilehealthsports.vaccinepass.util.ScaledBitmapLoader
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class UserCreationActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val navigationService: NavigationService by inject { parametersOf(this) }
    private lateinit var binding: ActivityUserCreationBinding
    private val viewModel: UserCreationViewModel by stateViewModel()
    private var currentPhotoPath: String = ""

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentPhotoPath = savedInstanceState.getString("currentPhotoPath", "")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentPhotoPath", currentPhotoPath)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_user_creation
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        messageService.subscribeToRequests(viewModel.messageRequest)
        navigationService.subscribeToRequests(viewModel.navigationRequest)
        disposables.addAll(messageService, navigationService)

        viewModel.themeCallback = { themeId ->
            application?.setTheme(themeId)
        }

        viewModel.errorText.value = getString(R.string.fragment_user_creation_error_required)
        viewModel.errorTextValidity.value = getString(R.string.fragment_user_creation_error_invalid)

        binding.fragmentUserCreationBirthDate.setOnClickListener {
            val c = Calendar.getInstance()

            val dpd = DatePickerDialog(
                this,
                R.style.SpinnerDatePickerStyle,
                { _, year, month, dayOfMonth ->
                    viewModel.setBirthDate(LocalDate.of(year, month + 1, dayOfMonth))
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }

        viewModel.permissionRequest.observe(this, {
            application?.let { ctx ->
                if (EasyPermissions.hasPermissions(ctx, Manifest.permission.CAMERA)) {
                    dispatchTakePictureIntent()
                } else {
                    // Do not have permissions, request them now
                    EasyPermissions.requestPermissions(
                        this,
                        "Access to camera is needed for this photo",
                        1234,
                        Manifest.permission.CAMERA
                    )
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        dispatchTakePictureIntent()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        messageService.executeRequest(ToastRequest("Permission denied"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            viewModel.currentPhotoPath.value = currentPhotoPath

            try {
                ScaledBitmapLoader.setPic(currentPhotoPath, 80, 80, binding.fragmentUserCreationPhoto)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
            viewModel.photoTaken.value = true
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val today = LocalDateTime.now()
        val timeStamp = today.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))

        // path that is returned corresponds to the path specified in res/xml/file_paths.xml
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    messageService.executeRequest(ToastRequest("Could not create photo file"))
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.mobilehealthsports.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }

                if (photoFile == null) messageService.executeRequest(ToastRequest("Could not take picture"))
            }
        }
    }

    override fun onDestroy() {
        disposables.dispose()
        viewModel.themeCallback = null
        super.onDestroy()
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1

        // create intent to navigate to this class
        fun intent(context: Context): Intent {
            return Intent(context, UserCreationActivity::class.java)
        }
    }
}
