package com.mobilehealthsports.vaccinepass.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.snackbar.Snackbar
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.ActivityMainBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf


class UserFragment : Fragment(R.layout.user_fragment)
class VaccineFragment : Fragment(R.layout.vaccine_fragment)
class AddVaccineFragment : Fragment(R.layout.add_fragment)
class CalendarFragment : Fragment(R.layout.calendar_fragment)
class SettingsFragment : Fragment(R.layout.settings_fragment)

class MainActivity : AppCompatActivity() {
    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val viewModel: MainViewModel by stateViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        messageService.subscribeToRequests(viewModel.messageRequest)
        disposables.addAll(messageService)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<UserFragment>(R.id.fragment_container_view)
        }


        val radius = resources.getDimension(R.dimen.activity_main_bottom_corner_radius)
        val bottomNavigationBackground = binding.bottomNavigation.background as MaterialShapeDrawable
        bottomNavigationBackground.shapeAppearanceModel =
                bottomNavigationBackground.shapeAppearanceModel.toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED, radius)
                        .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                        .build()

       binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.bottom_nav_user -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<UserFragment>(R.id.fragment_container_view)
                    }
                    true
                }
                R.id.bottom_nav_vaccine -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<VaccineFragment>(R.id.fragment_container_view)
                    }
                    true
                }
                R.id.bottom_nav_calendar -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<CalendarFragment>(R.id.fragment_container_view)
                    }
                    true
                }
                R.id.bottom_nav_settings -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<SettingsFragment>(R.id.fragment_container_view)
                    }
                    true
                }
                else -> false
            }
        }

        binding.ivAdd.setOnClickListener{
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<AddVaccineFragment>(R.id.fragment_container_view)
            }
        }

    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    companion object {
        // create intent to navigate to this class
        fun intent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}