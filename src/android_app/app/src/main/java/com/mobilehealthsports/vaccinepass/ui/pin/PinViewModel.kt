package com.mobilehealthsports.vaccinepass.ui.pin

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper.set
import kotlinx.coroutines.*


class PinViewModel : BaseViewModel() {

    enum class PinState {
        INITIAL,
        CONFIRM,
        CHECK
    }

    val messageRequest = ServiceRequest<MessageRequest>()

    private var pinLength = 4
    private var currentErrorCoroutine: Job? = null

    private var _titleColor = MutableLiveData(R.color.black)
    var titleColor: LiveData<Int> = _titleColor

    private var _pinState = MutableLiveData(PinState.CHECK)
    var pinState: LiveData<PinState> = _pinState

    private var _titleText = MutableLiveData(R.string.pin_title)
    val titleText: LiveData<Int> = _titleText

    private var _bottomText = MutableLiveData(R.string.pin_title_forgot_pin)
    val bottomText: LiveData<Int> = _bottomText

    private var _pin = MutableLiveData("")
    private var _pinConfirm = MutableLiveData("")

    private var _pinCount = MutableLiveData(0)
    val pinCount: LiveData<Int> = _pinCount

    private var _correctPin = MutableLiveData(false)
    val correctPin: LiveData<Boolean> = _correctPin

    lateinit var sharedPreferences: SharedPreferences

    init {
        setText()
    }

    private fun savePin() {
        sharedPreferences.apply {
            this["PIN"] = _pin.value
        }
    }

    private fun setText() {
        when (_pinState.value) {
            PinState.INITIAL -> {
                _titleText.value = R.string.pin_title
            }
            PinState.CONFIRM -> {
                _titleText.value = R.string.pin_title_confirm
            }
            PinState.CHECK -> {
                _titleText.value = R.string.pin_title
                _bottomText.value = R.string.pin_title_forgot_pin
            }
        }
    }

    fun onPin(digit: Char) {

        when (_pinState.value) {
            PinState.INITIAL -> {
                _pin.value = _pin.value.plus(digit)

                _pin.value?.let { pin ->
                    val count = pin.count()
                    _pinCount.value = count

                    if (pin.count() >= pinLength) {
                        viewModelScope.launch(Dispatchers.Main) {
                            delay(DELAY_MS)

                            _pinState.value = PinState.CONFIRM
                            setText()
                            _pinCount.value = 0
                        }
                    }
                }
            }
            PinState.CONFIRM -> {
                _pinConfirm.value = _pinConfirm.value.plus(digit)

                _pinConfirm.value?.let { pin ->
                    _pinCount.value = pin.count()

                    if (pin.count() >= pinLength) {
                        viewModelScope.launch(Dispatchers.Main) {
                            delay(DELAY_MS)
                            if (_pin.value.equals(_pinConfirm.value)) {
                                savePin()
                                _pinConfirm.value = ""
                                _pinCount.value = 0
                                _correctPin.value = true

                            } else {
                                _pinConfirm.value = ""
                                _pinCount.value = 0

                                currentErrorCoroutine?.cancel()
                                currentErrorCoroutine = GlobalScope.launch {
                                    // important: call postValue since its also asynchronous
                                    _titleColor.postValue(ERROR_COLOR)
                                    _titleText.postValue(R.string.pin_title_error)
                                    delay(ERROR_DELAY)
                                    _titleColor.postValue(DEFAULT_COLOR)
                                    _titleText.postValue(R.string.pin_title_confirm)
                                }
                            }
                        }
                    }
                }
            }
            PinState.CHECK -> {
                _pin.value = _pin.value.plus(digit)

                _pin.value?.let { pin ->
                    _pinCount.value = pin.count()

                    viewModelScope.launch(Dispatchers.Main) {
                        delay(DELAY_MS)
                        if (pin.count() >= pinLength) {
                            val correctPin =
                                sharedPreferences.getString(SHARED_PREF_KEY, "").equals(pin)

                            _correctPin.value = correctPin
                            _pin.value = ""
                            _pinCount.value = 0

                            if (!correctPin) {
                                currentErrorCoroutine?.cancel()
                                currentErrorCoroutine = GlobalScope.launch {
                                    // important: call postValue since its also asynchronous
                                    _titleColor.postValue(ERROR_COLOR)
                                    _titleText.postValue(R.string.pin_title_error)
                                    delay(ERROR_DELAY)
                                    _titleColor.postValue(DEFAULT_COLOR)
                                    _titleText.postValue(R.string.pin_title)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteLastEntry() {
        if (_pinState.value == PinState.CONFIRM) {
            removeLastCharacter(_pinConfirm)
        } else {
            removeLastCharacter(_pin)
        }
    }

    private fun removeLastCharacter(data: MutableLiveData<String>) {
        data.value?.let { pin ->
            val count = pin.count()

            if (count >= 1) {
                data.value = pin.substring(0, count - 1)
                _pinCount.value = count - 1
            }
        }
    }

    fun setInitialState(state: PinState) {
        this._pinState.value = state
    }

    fun setPinLength(pinLength: Int) {
        this.pinLength = pinLength
    }

    fun forgotPin() {
        currentErrorCoroutine?.cancel()

        when (_pinState.value) {
            PinState.CONFIRM -> {
                _pin.value = ""
                _pinConfirm.value = ""
                _pinCount.value = 0
                _titleColor.value = DEFAULT_COLOR
                _pinState.value = PinState.INITIAL
                setText()
            }
            PinState.CHECK -> {
                // TODO: reset app here
            }
            else -> {
            }
        }
    }

    companion object {
        private const val DELAY_MS = 200L
        const val SHARED_PREF_KEY = "PIN"
        const val DEFAULT_COLOR = R.color.black
        const val ERROR_COLOR = R.color.error_red
        const val ERROR_DELAY = 3000L
    }
}