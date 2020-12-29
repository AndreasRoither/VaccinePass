package com.mobilehealthsports.vaccinepass.ui.main.vaccine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.FragmentVaccineBinding
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationService
import com.mobilehealthsports.vaccinepass.ui.main.MainActivity
import com.mobilehealthsports.vaccinepass.ui.main.MainViewModel
import com.mobilehealthsports.vaccinepass.ui.main.user.VaccineState
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf

class VaccineFragment : Fragment() {
    private var disposables = CompositeDisposable()
    private val navigationService: NavigationService by inject { parametersOf(this) }
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: VaccineViewModel by stateViewModel()

    private lateinit var binding: FragmentVaccineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vaccine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentVaccineBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        binding.popupBtn.setOnClickListener { v: View ->
            showMenu(v, R.menu.sort_by_menu)
        }

        viewModel.noVaccinationsString.value = getString(R.string.vaccine_nothing_found)

        mainViewModel.user.observe(viewLifecycleOwner, { user ->
            user?.let {
                viewModel.setUser(it)
            }
        })

        viewModel.selectedId.observe(viewLifecycleOwner, {
            if (it < 0) return@observe

            when (activity) {
                is MainActivity -> {
                    (activity as MainActivity).replaceWithVaccinationFragment(it)
                }
            }
        })

        disposables.addAll(navigationService)
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireActivity(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            // Respond to menu item click.
            when (menuItem.itemId) {
                R.id.sort_by_option_1 -> {
                    viewModel.setVaccinationType(VaccineState.ALL)
                }
                R.id.sort_by_option_2 -> {
                    viewModel.setVaccinationType(VaccineState.SCHEDULED)
                }
                R.id.sort_by_option_3 -> {
                    viewModel.setVaccinationType(VaccineState.NOT_SCHEDULED)
                }
                R.id.sort_by_option_4 -> {
                    viewModel.setVaccinationType(VaccineState.NOT_VACCINATED)
                }
            }
            true
        }

        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }

        // Show the popup menu.
        popup.show()
    }
}