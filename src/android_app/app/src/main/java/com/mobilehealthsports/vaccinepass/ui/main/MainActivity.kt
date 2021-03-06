package com.mobilehealthsports.vaccinepass.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.coroutineScope
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.repository.UserRepository
import com.mobilehealthsports.vaccinepass.databinding.ActivityMainBinding
import com.mobilehealthsports.vaccinepass.ui.main.add_vaccine.AddVaccineFragment
import com.mobilehealthsports.vaccinepass.ui.main.add_vaccine.ScanQrCodeActivity
import com.mobilehealthsports.vaccinepass.ui.main.add_vaccine.ScanQrCodeFragment
import com.mobilehealthsports.vaccinepass.ui.main.calendar.CalendarFragment
import com.mobilehealthsports.vaccinepass.ui.main.settings.SettingsFragment
import com.mobilehealthsports.vaccinepass.ui.main.user.UserFragment
import com.mobilehealthsports.vaccinepass.ui.main.vaccine.VaccineFragment
import com.mobilehealthsports.vaccinepass.ui.vaccination.VaccinationFragment
import com.mobilehealthsports.vaccinepass.util.BaseActivity
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by stateViewModel()
    private val sharedPreferences: SharedPreferences by inject()
    private val userRepository: UserRepository by inject()
    private var oldNavigationItem = 1
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding.lifecycleOwner = this

        // lookup current user
        val lastUserId: Long = sharedPreferences[PreferenceHelper.LAST_USER_ID_PREF, -1L]!!
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            userRepository.getUser(lastUserId)?.let {
                // we are in the background thread, and mutable live data can only be set synchronous
                viewModel.user.postValue(it)
            }
        }

        supportFragmentManager.commit {
            setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            setReorderingAllowed(true)
            add<UserFragment>(R.id.fragment_container_view)
        }

        val radius = resources.getDimension(R.dimen.activity_main_bottom_corner_radius)
        val bottomNavigationBackground =
            binding.bottomNavigation.background as MaterialShapeDrawable
        bottomNavigationBackground.shapeAppearanceModel =
            bottomNavigationBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_user -> {
                    closeAddVaccineFragmentIfPossible()
                    hideAddVaccineButtons()
                    supportFragmentManager.commit {
                        if (oldNavigationItem > 1) {
                            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        }

                        setReorderingAllowed(true)
                        replace<UserFragment>(R.id.fragment_container_view)
                    }
                    oldNavigationItem = 1
                    true
                }
                R.id.bottom_nav_vaccine -> {
                    closeAddVaccineFragmentIfPossible()
                    hideAddVaccineButtons()
                    supportFragmentManager.commit {
                        if (oldNavigationItem > 2) {
                            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        } else {
                            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        }

                        setReorderingAllowed(true)
                        replace<VaccineFragment>(R.id.fragment_container_view)
                    }
                    oldNavigationItem = 2
                    true
                }
                R.id.bottom_nav_calendar -> {
                    closeAddVaccineFragmentIfPossible()
                    hideAddVaccineButtons()
                    supportFragmentManager.commit {
                        if (oldNavigationItem > 3) {
                            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        } else {
                            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        }
                        setReorderingAllowed(true)
                        replace<CalendarFragment>(R.id.fragment_container_view)
                    }
                    oldNavigationItem = 3
                    true
                }
                R.id.bottom_nav_settings -> {
                    closeAddVaccineFragmentIfPossible()
                    hideAddVaccineButtons()
                    supportFragmentManager.commit {
                        if (oldNavigationItem < 4) {
                            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        }
                        setReorderingAllowed(true)
                        replace<SettingsFragment>(R.id.fragment_container_view)
                    }
                    oldNavigationItem = 4
                    true
                }
                else -> false
            }
        }

        binding.ivAdd.setOnClickListener {
            val gotDismissed = closeAddVaccineFragmentIfPossible()
            if (gotDismissed) {
                //do nothing - I know bad design

            } else if (binding.addVacQr.visibility == View.GONE) {
                binding.addVacQr.visibility = View.VISIBLE
               // binding.addVacManual.visibility = View.VISIBLE
                binding.checkVaccination.visibility = View.VISIBLE

            } else {
                hideAddVaccineButtons()
            }
        }

       /* binding.addVacManual.setOnClickListener {
            hideAddVaccineButtons()

            binding.ivAdd.setBackgroundResource(R.drawable.drawable_btn_background)
            supportFragmentManager.commit {
                setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                setReorderingAllowed(true)
                add<AddVaccineFragment>(R.id.dialog_container_view, AddVaccineFragment.TAG)
            }
        }*/

        binding.addVacQr.setOnClickListener {
            hideAddVaccineButtons()

            val intent = Intent(this, ScanQrCodeActivity::class.java)
            startActivity(intent)
        }

        binding.checkVaccination.setOnClickListener {
            hideAddVaccineButtons()
            supportFragmentManager.commit {
                setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                setReorderingAllowed(true)
                replace<ScanQrCodeFragment>(R.id.fragment_container_view, ScanQrCodeFragment.TAG)
            }
        }
    }

    fun replaceWithVaccinationFragment(id: Long) {
        val fragment = VaccinationFragment.newInstance(id)

        supportFragmentManager.commit {
            replace(R.id.fragment_container_view, fragment)
        }
    }

    private fun closeAddVaccineFragmentIfPossible(): Boolean {
        var gotDismissed = false;
        val fragments = supportFragmentManager.fragments
        for (f in fragments) {
            if (f is AddVaccineFragment) {
                //somehow popBackStackImmediate is not working
                f.dismissDialog()
                gotDismissed = true;
            }
        }
        return gotDismissed
    }

    private fun hideAddVaccineButtons() {
        binding.addVacQr.visibility = View.GONE
        binding.checkVaccination.visibility = View.GONE
    }

    companion object {

        // create intent to navigate to this class
        fun intent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}