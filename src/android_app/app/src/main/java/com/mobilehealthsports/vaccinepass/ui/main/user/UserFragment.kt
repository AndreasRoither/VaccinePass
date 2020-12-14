package com.mobilehealthsports.vaccinepass.ui.main.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.FragmentUserBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.ui.main.MainViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class UserFragment : Fragment(){
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val viewModel: UserViewModel by stateViewModel()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: VaccineViewAdapter

    private lateinit var fragmentUserBinding : FragmentUserBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentUserBinding.bind(view)
        fragmentUserBinding = binding

        adapter = VaccineViewAdapter(viewModel.listItems)
        viewModel.setUser(mainViewModel.user)

        binding.adapter = adapter
        binding.viewModel = viewModel
        //binding.vaccineRecyclerview.adapter = adapter
        binding.lifecycleOwner = this

        messageService.subscribeToRequests(viewModel.messageRequest)
        disposables.addAll(messageService)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}