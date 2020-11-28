package com.mobilehealthsports.vaccinepass.ui.main.add_vaccine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.FragmentAddBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class AddVaccineFragment : DialogFragment() {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val viewModel: AddViewModel by stateViewModel()
    private lateinit var fragmentAddBinding :  FragmentAddBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddBinding.bind(view)
        fragmentAddBinding = binding

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.addVaccineBtnAdd.setOnClickListener{
            dismissDialog()
        }

        binding.addVaccineBtnCancel.setOnClickListener {
            dismissDialog()
        }

        messageService.subscribeToRequests(viewModel.messageRequest)
        disposables.addAll(messageService)
    }

    private fun dismissDialog() {
        val button = activity?.findViewById<ImageView>(R.id.iv_add)
        button?.isEnabled = true
        button?.background = null
        dismiss()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
    companion object{
        const val TAG = "AddVaccineBottomSheet"
    }
}