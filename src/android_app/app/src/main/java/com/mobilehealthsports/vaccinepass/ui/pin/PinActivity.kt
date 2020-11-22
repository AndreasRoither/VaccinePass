package com.mobilehealthsports.vaccinepass.ui.pin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.ActivityPinBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class PinActivity : AppCompatActivity() {
    private lateinit var disposables: CompositeDisposable
    private val messageService: MessageService by inject { parametersOf(this) }
    private val viewModel: PinViewModel by stateViewModel()

    private val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        this,
        "VaccinePass",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityPinBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_pin
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        messageService.subscribeToRequests(viewModel.messageRequest)
        viewModel.sharedPreferences = sharedPreferences

        when (val state = intent.getSerializableExtra(EXTRA_STATE)) {
            is PinViewModel.PinState -> {
                viewModel.setInitialState(state)
            }
        }

        val length = intent.getIntExtra(EXTRA_PIN_LENGTH, 4)
        viewModel.setPinLength(length)

        disposables.addAll(messageService)

        setContentView(R.layout.activity_pin)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_STATE = "PinState"
        const val EXTRA_PIN_LENGTH = "PinLength"

        // create intent to navigate to this class
        fun intent(context: Context, state: PinViewModel.PinState, pinLength: Int): Intent {
            return Intent(context, PinActivity::class.java).apply {
                this.putExtra(EXTRA_STATE, state)
                this.putExtra(EXTRA_PIN_LENGTH, pinLength)
            }
        }
    }
}