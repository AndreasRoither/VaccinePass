package com.mobilehealthsports.vaccinepass.presentation.services.navigation

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.mobilehealthsports.vaccinepass.TestActivity
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.ui.main.MainActivity
import com.mobilehealthsports.vaccinepass.ui.pin.PinActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AppNavigationService private constructor(
    private val currentActivity: FragmentActivity? = null,
    private val currentFragment: Fragment? = null
) : NavigationService {

    constructor(activity: FragmentActivity) : this(currentActivity = activity)
    constructor(fragment: Fragment) : this(currentFragment = fragment)

    /**
     * Context can come from either fragment or activity, if the fragment is null
     * the activity is used
     */
    private val context
        get() = currentFragment?.context ?: activity!!

    private val activity
        get() = currentActivity ?: currentFragment?.activity

    private val disposables = CompositeDisposable()

    private fun startIntent(intent: Intent) {
        context.startActivity(intent)
    }

    /**
     * Always check first if the fragment was the one who called
     */
    private fun startIntentForResult(intent: Intent, requestCode: Int) {
        if (currentFragment != null) {
            currentFragment.startActivityForResult(intent, requestCode)
        } else {
            currentActivity?.startActivityForResult(intent, requestCode)
        }
    }

    override fun subscribeToRequests(service: ServiceRequest<NavigationRequest>) {
        disposables.add(
            service.requests.observeOn(AndroidSchedulers.mainThread())
                .subscribe { navigationRequest ->
                    executeRequest(navigationRequest)
                }
        )
    }

    override fun executeRequest(request: NavigationRequest) {
        if (currentFragment == null && currentActivity == null)
            throw IllegalStateException("[VaccPass] Context is null for navigation request")

        when (request) {
            is HomeRequest -> startIntent(TestActivity.intent(context))
            is PinRequest -> startIntent(PinActivity.intent(context, request.state, request.pinLength))
            is MainRequest -> startIntent(MainActivity.intent(context))
        }
    }

    override fun dispose() {
        disposables.dispose()
    }

    override fun isDisposed(): Boolean {
        return disposables.isDisposed
    }
}