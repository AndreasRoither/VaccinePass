package com.mobilehealthsports.vaccinepass.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.ActivityMainBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val viewModel: MainViewModel by stateViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        messageService.subscribeToRequests(viewModel.messageRequest)
        disposables.addAll(messageService)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    companion object {
        // create intent to navigate to this class
        fun intent(context: Context): Intent {
            return Intent(context, MainActivity::class.java).apply {
            }
        }
    }
}