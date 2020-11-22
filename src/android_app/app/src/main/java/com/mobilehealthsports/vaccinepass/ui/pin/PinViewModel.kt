package com.mobilehealthsports.vaccinepass.ui.pin

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.SnackbarRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper.set


class PinViewModel : BaseViewModel() {

    enum class PinState {
        INITIAL,
        CONFIRM,
        CHECK
    }

    val messageRequest = ServiceRequest<MessageRequest>()

    private var pinLength = 4
    private var state: PinState = PinState.CHECK

    private var _titleText = MutableLiveData("")
    val titleText: LiveData<String> = _titleText

    private var _bottomText = MutableLiveData("")
    val bottomText: LiveData<String> = _bottomText

    private var _pin = MutableLiveData("")
    val pin: LiveData<String> = _pin

    private var _pinConfirm = MutableLiveData("")
    val pinConfirm: LiveData<String> = _pinConfirm

    private var _correctPin = MutableLiveData(false)
    private val correctPin: LiveData<Boolean> = _correctPin

    lateinit var sharedPreferences: SharedPreferences

    private fun savePin() {
        sharedPreferences.apply {
            this["PIN"] = _pin.value
        }
    }

    private fun reset() {
        _pin.value = ""
        _pinConfirm.value = ""
    }

    fun onPin(digit: Char) {

        when (state) {
            PinState.INITIAL -> {
                _pin.value = _pin.value.plus(digit)

                _pin.value?.let { pin ->
                    if (pin.count() >= pinLength) state = PinState.CONFIRM
                }

            }
            PinState.CONFIRM -> {
                _pinConfirm.value = _pinConfirm.value.plus(digit)

                _pinConfirm.value?.let { pin ->
                    if (pin.count() >= pinLength) {
                        if (_pin.value.equals(_pinConfirm.value)) {
                            savePin()
                            messageRequest.request(SnackbarRequest("Same Pin!"))
                            reset()
                        } else {
                            messageRequest.request(SnackbarRequest("Wrong Pin!"))
                            reset()
                        }
                    }
                }
            }
            PinState.CHECK -> {
                _pin.value?.let { pin ->
                    if (pin.count() >= pinLength) {
                        _correctPin.value = sharedPreferences.getString("PIN", "").equals(pin)
                        reset()
                    }
                }
            }
        }
    }

    fun deleteLastEntry() {
        _pin.value?.let { pin ->
            if (pin.count() >= 1) {
                _pin.value = pin.substring(0, pin.count() - 1)
            }
        }
    }

    fun setInitialState(state: PinState) {
        this.state = state
    }

    fun setPinLength(pinLength: Int) {
        this.pinLength = pinLength
    }

    fun setTitleText(text: String) {
        _titleText.value = text
    }

    fun setBottomText(text: String) {
        _titleText.value = text
    }
}