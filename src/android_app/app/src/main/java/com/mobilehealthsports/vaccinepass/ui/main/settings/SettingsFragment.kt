package com.mobilehealthsports.vaccinepass.ui.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.FragmentSettingsBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationService
import com.mobilehealthsports.vaccinepass.ui.main.MainViewModel
import com.mobilehealthsports.vaccinepass.util.ScaledBitmapLoader
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class SettingsFragment : Fragment() {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val navigationService: NavigationService by inject { parametersOf(this) }
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: SettingsViewModel by stateViewModel()
    private lateinit var fragmentSettingsBinding: FragmentSettingsBinding
    private lateinit var adapter: SettingsViewAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSettingsBinding.bind(view)

        fragmentSettingsBinding = binding

        adapter = SettingsViewAdapter(viewModel.listItems)

        navigationService.subscribeToRequests(viewModel.navigationRequest)

        binding.cardClicked = viewModel.CardClickListener()

        binding.viewModel = viewModel
        binding.adapter = adapter
        binding.lifecycleOwner = requireActivity()

        mainViewModel.user.observe(viewLifecycleOwner, { user ->
            user?.let {
                viewModel.setUser(it)
                it.photoPath?.let { photoPath ->
                    try {
                        ScaledBitmapLoader.setPic(photoPath, 80, 80, binding.fragmentSettingsUserPhoto)
                    } catch (ex: Exception) {
                        Timber.e(ex)
                    }
                }
            }
        })

        messageService.subscribeToRequests(viewModel.messageRequest)
        disposables.addAll(messageService,navigationService)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}