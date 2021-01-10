package com.mobilehealthsports.vaccinepass.ui.main.add_calendar_entry

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.FragmentAddCalendarEntryBinding
import com.mobilehealthsports.vaccinepass.ui.main.calendar.CalendarFragment
import com.mobilehealthsports.vaccinepass.util.livedata.NonNullMutableLiveData
import com.mobilehealthsports.vaccinepass.util.notification.AlarmScheduler
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import java.time.LocalDate
import java.util.*

class CalendarEntryFragment : Fragment() {
    private lateinit var binding: FragmentAddCalendarEntryBinding
    private val viewModel: CalendarEntryViewModel by stateViewModel()
    private var currentDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            currentDate = it.getString(CalendarFragment.SELECTED_DATE, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_calendar_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddCalendarEntryBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()

        viewModel.appointmentDate = NonNullMutableLiveData(LocalDate.parse(currentDate))

        viewModel.errorText.value = getString(R.string.fragment_user_creation_error_required)
        viewModel.errorTextValidity.value = getString(R.string.fragment_user_creation_error_invalid)

        viewModel.cancelAdd.observe(viewLifecycleOwner, {
            if (it) {
                requireActivity().supportFragmentManager.commit {
                    replace<CalendarFragment>(R.id.fragment_container_view)
                }
            }
        })

        viewModel.addAppointment.observe(viewLifecycleOwner, {
            if (it) {
                requireActivity().supportFragmentManager.commit {
                    replace<CalendarFragment>(R.id.fragment_container_view)
                }
            }
        })

        viewModel.addReminder.observe(viewLifecycleOwner, {
            if (it == null) return@observe

            AlarmScheduler.scheduleAlarmForReminder(requireContext(), it)
        })

        binding.appointmentDate.setOnClickListener {
            val c = Calendar.getInstance()

            val dpd = DatePickerDialog(
                requireContext(),
                R.style.SpinnerDatePickerStyle,
                { _, year, month, dayOfMonth ->
                    viewModel.appointmentDate.value = LocalDate.of(year, month + 1, dayOfMonth);
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }

        binding.reminderDate.setOnClickListener {
            val c = Calendar.getInstance()

            val dpd = DatePickerDialog(
                requireContext(),
                R.style.SpinnerDatePickerStyle,
                { _, year, month, dayOfMonth ->
                    viewModel.reminderDate.value = LocalDate.of(year, month + 1, dayOfMonth);
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(date: String) =
            CalendarEntryFragment().apply {
                arguments = Bundle().apply {
                    putString(CalendarFragment.SELECTED_DATE, date)
                }
            }
    }

}