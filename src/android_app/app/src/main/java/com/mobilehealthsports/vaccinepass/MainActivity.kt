package com.mobilehealthsports.vaccinepass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.mobilehealthsports.vaccinepass.databinding.ActivityMainBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private lateinit var disposables: CompositeDisposable
    private val messageService: MessageService by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        disposables = CompositeDisposable()
        //messageService.subscribeToRequests(viewModel.messageRequest)
        // in viewModel:
        // val messageRequest = ServiceRequest<MessageRequest>()
        //  messageRequest.raise(whatever)

        disposables.add(messageService)

        // The layout for this activity is a Data Binding layout so it needs to be inflated using
        // DataBindingUtil.
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        binding.fab.setOnClickListener{view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    companion object {
        // create intent to navigate to this class
        fun intent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}