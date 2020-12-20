package com.mobilehealthsports.vaccinepass.ui.main.user

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.repository.UserRepository
import com.mobilehealthsports.vaccinepass.databinding.FragmentUserBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationService
import com.mobilehealthsports.vaccinepass.ui.main.MainViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class UserFragment : Fragment() {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val navigationService: NavigationService by inject { parametersOf(this) }
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: UserViewModel by stateViewModel()
    private val userRepository: UserRepository by inject()
    private val sharedPreferences: SharedPreferences by inject()

    private lateinit var fragmentUserBinding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentUserBinding.bind(view)
        fragmentUserBinding = binding

        viewModel.setUser(mainViewModel.user)

        navigationService.subscribeToRequests(viewModel.navigationRequest)

        binding.cardClick = viewModel.CardClicked()

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()

        messageService.subscribeToRequests(viewModel.messageRequest)
        disposables.addAll(messageService,navigationService)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}