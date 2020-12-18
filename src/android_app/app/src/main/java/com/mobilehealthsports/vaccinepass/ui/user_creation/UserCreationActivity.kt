package com.mobilehealthsports.vaccinepass.ui.user_creation

import android.Manifest
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.ActivityUserCreationBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.presentation.services.messages.ToastRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf
import pub.devrel.easypermissions.EasyPermissions
import java.time.LocalDate
import java.util.*

class UserCreationActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val navigationService: NavigationService by inject { parametersOf(this) }
    private lateinit var binding: ActivityUserCreationBinding
    private val viewModel: UserCreationViewModel by stateViewModel()

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
            val imageBitmap = data?.extras?.get("data") as Bitmap

            application?.let { ctx ->
                binding.fragmentUserCreationPhotoBtn.background =
                    imageBitmap.toDrawable(ctx.resources)
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            messageService.executeRequest(ToastRequest("Could not start camera"))
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
