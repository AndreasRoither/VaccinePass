package com.mobilehealthsports.vaccinepass.ui.testing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.models.Vaccination
import com.mobilehealthsports.vaccinepass.business.repository.VaccinationRepository
import com.mobilehealthsports.vaccinepass.business.repository.VaccinationRepositoryImpl
import com.mobilehealthsports.vaccinepass.databinding.ActivityFragmentTestBinding
import com.mobilehealthsports.vaccinepass.ui.main.calendar.CalendarFragment
import com.mobilehealthsports.vaccinepass.ui.vaccination.VaccinationFragment
import com.mobilehealthsports.vaccinepass.util.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDate


class FragmentTestActivity:
    BaseActivity() {

    private val vaccinationRepository: VaccinationRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFragmentTestBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_fragment_test)

        println(intent.getStringExtra("fragment"))

        val fragment = when (intent.getStringExtra("fragment")) {
            "CalendarFragment" -> {
                CalendarFragment.newInstance()
            }
            "vaccineFragment" -> {
                VaccinationFragment.newInstance(1)
            }
            "AddVaccination" -> {
                val vaccination = Vaccination(
                    1, 1, true, LocalDate.of(2025, 12, 3), "1",
                    LocalDate.of(2020, 12, 20), "5 years", "1", "Test",
                    "{\n" +
                            "    \"name\": \"Maike\",\n" +
                            "    \"password\": \"123\",\n" +
                            "    \"mail\": \"maike.rieger@gmail.at\"\n" +
                            "}"
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    vaccinationRepository.insertVaccination(vaccination)
                }
                VaccinationFragment.newInstance(1)
            }
            else -> CalendarFragment.newInstance()
        }

        supportFragmentManager.commit {
            replace(R.id.activity_test_fragment_container, fragment)
        }
    }

    companion object {
        // create intent to navigate to this class
        fun intent(context: Context, fragment: String): Intent {
            return Intent(context, FragmentTestActivity::class.java).apply {
                putExtra("fragment", fragment)
            }
        }
    }
}