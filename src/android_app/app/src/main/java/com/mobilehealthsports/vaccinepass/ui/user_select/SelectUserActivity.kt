package com.mobilehealthsports.vaccinepass.ui.user_select

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.ActivitySelectUserBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationService
import com.mobilehealthsports.vaccinepass.util.BaseActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class SelectUserActivity  : BaseActivity() {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val navigationService: NavigationService by inject { parametersOf(this) }
    private val viewModel: SelectUserViewModel by stateViewModel()
    private lateinit var adapter: SelectUserViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySelectUserBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_select_user
        )
        binding.viewModel = viewModel


        navigationService.subscribeToRequests(viewModel.navigationRequest)

        adapter = SelectUserViewAdapter(viewModel.userList, viewModel.ItemClickListener())

        binding.selectUserRecyclerview.adapter = adapter
        binding.lifecycleOwner = this

        messageService.subscribeToRequests(viewModel.messageRequest)
        disposables.addAll(messageService,navigationService)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    companion object {
        // create intent to navigate to this class
        fun intent(context: Context): Intent {
            return Intent(context, SelectUserActivity::class.java)
        }
    }
}