package com.mobilehealthsports.vaccinepass.ui.testing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.ActivityFragmentTestBinding
import com.mobilehealthsports.vaccinepass.ui.main.calendar.CalendarFragment
import com.mobilehealthsports.vaccinepass.util.BaseActivity


class FragmentTestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFragmentTestBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_fragment_test)

        println(intent.getStringExtra("fragment"))

        val fragment = when (intent.getStringExtra("fragment")) {
            "CalendarFragment" -> {
                CalendarFragment.newInstance()
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