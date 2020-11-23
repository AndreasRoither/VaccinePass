package com.mobilehealthsports.vaccinepass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.mobilehealthsports.vaccinepass.databinding.ActivityTestBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationService
import com.mobilehealthsports.vaccinepass.ui.pin.PinViewModel
import com.mobilehealthsports.vaccinepass.ui.testing.TestViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class TestActivity : AppCompatActivity() {

    private var disposables = CompositeDisposable()
    private val navigationService: NavigationService by inject { parametersOf(this) }
    private val viewModel: TestViewModel by stateViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigationService.subscribeToRequests(viewModel.navigationRequest)
        disposables.add(navigationService)

        // The layout for this activity is a Data Binding layout so it needs to be inflated using
        // DataBindingUtil.
        val binding: ActivityTestBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_test)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        binding.viewModel = viewModel
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    companion object {
        // create intent to navigate to this class
        fun intent(context: Context): Intent {
            return Intent(context, TestActivity::class.java)
        }
    }
}