package com.mobilehealthsports.vaccinepass.ui.introduction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.ActivityIntroductionBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.util.BaseActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class IntroductionActivity : BaseActivity() {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val viewModel: IntroductionViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityIntroductionBinding = DataBindingUtil.setContentView(this, R.layout.activity_introduction)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        messageService.subscribeToRequests(viewModel.messageRequest)
        disposables.addAll(messageService)

        viewModel.okClick.observe(this, {
            if (it) {
                setResult(RESULT_OK)
                finish()
            }
        })
    }

    companion object {

        // create intent to navigate to this class
        fun intent(context: Context): Intent {
            return Intent(context, IntroductionActivity::class.java)
        }
    }
}