package com.mobilehealthsports.vaccinepass.ui.main.add_vaccine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mobilehealthsports.vaccinepass.databinding.FragmentAddBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class AddVaccineFragment : Fragment() {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val viewModel: AddViewModel by stateViewModel()

    private var fragmentAddBinding :  FragmentAddBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = FragmentAddBinding.bind(view)
        fragmentAddBinding = binding

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        messageService.subscribeToRequests(viewModel.messageRequest)
        disposables.addAll(messageService)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}