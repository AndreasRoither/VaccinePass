package com.mobilehealthsports.vaccinepass.ui.main.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.FragmentCalendarBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationService
import com.mobilehealthsports.vaccinepass.ui.main.add_calendar_entry.CalendarEntryFragment
import com.mobilehealthsports.vaccinepass.util.daysOfWeekFromLocale
import com.mobilehealthsports.vaccinepass.util.setTextColorRes
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf
import java.time.format.TextStyle
import java.util.*

class CalendarFragment : Fragment() {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val navigationService: NavigationService by inject { parametersOf(this) }

    private val viewModel: CalendarViewModel by stateViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCalendarBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        messageService.subscribeToRequests(viewModel.messageRequest)
        navigationService.subscribeToRequests(viewModel.navigationRequest)
        disposables.addAll(messageService, navigationService)

        val daysOfWeek = daysOfWeekFromLocale()
        binding.legendLayout.children.forEachIndexed { index, view1 ->
            (view1 as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.NARROW, Locale.ENGLISH)
                    .toUpperCase(Locale.ENGLISH)
                setTextColorRes(R.color.black)
            }
        }

        // setup binding
        viewModel.setupCalendar(binding.calendar)

        viewModel.addEntry.observe(this, {
            if (it) {
                requireActivity().supportFragmentManager.commit {
                    val fragment =
                        CalendarEntryFragment.newInstance(viewModel.selectedDate!!.toString())
                    replace(R.id.fragment_container_view, fragment)
                }
            }
        })
    }

    companion object {
        fun newInstance() = CalendarFragment()
        const val SELECTED_DATE = "SELECTED_DATE"
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}

