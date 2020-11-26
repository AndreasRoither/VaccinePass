package com.mobilehealthsports.vaccinepass.ui.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mobilehealthsports.vaccinepass.databinding.FragmentCalendarBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class CalendarFragment : Fragment() {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val navigationService: NavigationService by inject { parametersOf(this) }

    private val viewModel: CalendarViewModel by stateViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCalendarBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        messageService.subscribeToRequests(viewModel.messageRequest)
        navigationService.subscribeToRequests(viewModel.navigationRequest)

        disposables.addAll(messageService, navigationService)
    }


    companion object {
        fun newInstance() = CalendarFragment()
    }
}
