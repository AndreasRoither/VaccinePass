package com.mobilehealthsports.vaccinepass.ui.user_creation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.FragmentUserCreationBinding
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


class UserCreationFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val navigationService: NavigationService by inject { parametersOf(this) }
    private lateinit var binding: FragmentUserCreationBinding
    private val viewModel: UserCreationViewModel by stateViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUserCreationBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        messageService.subscribeToRequests(viewModel.messageRequest)
        navigationService.subscribeToRequests(viewModel.navigationRequest)
        disposables.addAll(messageService, navigationService)

        viewModel.themeCallback = { themeId ->
            context?.setTheme(themeId)
        }

        binding.fragmentUserCreationBirthDate.setOnClickListener {
            val c = Calendar.getInstance()

            val dpd = context?.let { ctx ->
                DatePickerDialog(
                    ctx,
                    R.style.SpinnerDatePickerStyle,
                    { _, year, month, dayOfMonth ->
                        viewModel.setBirthDate(LocalDate.of(year, month + 1, dayOfMonth))
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
                )
            }

            dpd?.show()
        }

        viewModel.permissionRequest.observe(viewLifecycleOwner, {
            context?.let { ctx ->
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            context?.let { ctx ->
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

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1

        @JvmStatic
        fun newInstance() = UserCreationFragment()
    }

    override fun onDestroy() {
        disposables.dispose()
        viewModel.themeCallback = null
        super.onDestroy()
    }
}