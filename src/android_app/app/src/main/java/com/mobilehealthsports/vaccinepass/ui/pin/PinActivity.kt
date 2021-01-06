package com.mobilehealthsports.vaccinepass.ui.pin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.ActivityPinBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.util.BaseActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class PinActivity : BaseActivity() {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val viewModel: PinViewModel by stateViewModel()
    private val pinList: MutableList<Boolean> = mutableListOf()
    private val pinListLine: MutableList<Int> = mutableListOf()
    private val adapter = PinViewAdapter(pinList)
    private val adapterLine = PinLineViewAdapter(pinListLine)
    private var lastPinCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityPinBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_pin
        )
        binding.viewModel = viewModel
        binding.pinRecyclerview.adapter = adapter
        binding.lineRecyclerview.adapter = adapterLine
        binding.lifecycleOwner = this

        messageService.subscribeToRequests(viewModel.messageRequest)
        disposables.addAll(messageService)

        val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            this,
            "VaccinePass",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        viewModel.sharedPreferences = sharedPreferences

        when (val state = intent.getSerializableExtra(EXTRA_STATE)) {
            is PinViewModel.PinState -> {
                viewModel.setInitialState(state)
            }
        }

        val length = intent.getIntExtra(EXTRA_PIN_LENGTH, 4)
        viewModel.setPinLength(length)
        val list = (1..length)
        pinListLine.addAll(list)
        list.forEach { _ ->
            pinList.add(false)
        }

        adapterLine.notifyDataSetChanged()

        viewModel.pinCount.observe(this, { pinCount ->

            if (pinCount > pinList.count()) return@observe

            // when pinCount is 0 all pins should be set invisible
            if (pinCount == 0) {
                pinList.replaceAll {
                    false
                }
                adapter.notifyDataSetChanged()
                lastPinCount = 0
                return@observe
            }

            // set either the removed pin invisible or the current pin visible
            // counting starts at 0
            if (lastPinCount > pinCount) {
                pinList[pinCount] = false
            } else {
                pinList[pinCount - 1] = true
            }

            //adapter.pins = pinList
            adapter.notifyDataSetChanged()
            lastPinCount = pinCount
        })

        viewModel.correctPin.observe(this, {
            if (it) {
                val data = Intent().apply {
                    putExtra("result", true)
                }
                setResult(RESULT_OK, data)
                finish()
            }
        })
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