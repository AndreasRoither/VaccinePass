package com.mobilehealthsports.vaccinepass.presentation.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

internal abstract class BaseViewModel : ViewModel() {
    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}