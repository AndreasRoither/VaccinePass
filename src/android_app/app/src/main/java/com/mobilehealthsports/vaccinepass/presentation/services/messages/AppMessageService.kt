package com.mobilehealthsports.vaccinepass.presentation.services.messages

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber


/**
 * Concrete class that shows dialogs / toast messages
 */
class AppMessageService private constructor(
    private val currentActivity: FragmentActivity? = null,
    private val currentFragment: Fragment? = null
) : MessageService {

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

    override fun displayToastMessage(messageId: Int) {
        try {
            displayToastMessage(context.getString(messageId))
        } catch (ex: Exception) {
            Timber.e(ex, "[VaccPass] Could not display toast message.")
        }
    }

    override fun displayToastMessage(message: String) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } catch (ex: Exception) {
            Timber.e(ex, "[VaccPass] Could not display toast message.")
        }
    }

    override fun displaySnackbar(
        view: View,
        msgId: Int,
        btnMsg: Int?,
        btnListener: DialogInterface.OnClickListener?
    ) {
        try {

            val btn = if (btnMsg != null) context.getString(btnMsg) else null
            displaySnackbar(view, context.getString(msgId), btn, btnListener)
        } catch (ex: Exception) {
            Timber.e(ex, "[VaccPass] Could not display snackbar.")
        }
    }

    override fun displaySnackbar(
        view: View,
        msg: String,
        btnMsg: String?,
        btnListener: DialogInterface.OnClickListener?
    ) {
        // TODO
        // yet to be decided
    }

    override fun displayDialog(
        titleId: Int,
        msgId: Int,
        positiveBtnId: Int,
        positiveBtnListener: DialogInterface.OnClickListener?,
        negativeBtnId: Int,
        negativeBtnListener: DialogInterface.OnClickListener?
    ) {
        try {
            val builder = AlertDialog.Builder(context)
            if (titleId != 0) {
                builder.setTitle(titleId)
            }
            if (msgId != 0) {
                builder.setMessage(msgId)
            }
            if (positiveBtnId != 0) {
                builder.setPositiveButton(positiveBtnId, positiveBtnListener)
            }
            if (negativeBtnId != 0) {
                builder.setNegativeButton(negativeBtnId, negativeBtnListener)
            }

            builder.show()
        } catch (ex: Exception) {
            Timber.e(ex, "[VaccPass] Could not display dialog with titleId $titleId")
        }
    }

    override fun displayDialog(
        title: String?,
        msg: String,
        positiveBtn: String?,
        positiveBtnListener: DialogInterface.OnClickListener?,
        negativeBtn: String?,
        negativeBtnListener: DialogInterface.OnClickListener?
    ) {
        try {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(msg)

            if (positiveBtn != null) {
                builder.setPositiveButton(positiveBtn, positiveBtnListener)
            }
            if (negativeBtn != null) {
                builder.setNegativeButton(negativeBtn, negativeBtnListener)
            }

            builder.show()
        } catch (ex: Exception) {
            Timber.e(ex, "[VaccPass] Could not display dialog with title $title")
        }
    }

    override fun subscribeToRequests(service: ServiceRequest<MessageRequest>) {
        disposables.add(service.requests.observeOn(AndroidSchedulers.mainThread())
            .subscribe { messageRequest ->
                executeRequest(messageRequest)
            }
        )
    }

    /**
     * Smart cast request and decide what message will be shown
     */
    override fun executeRequest(request: MessageRequest) {

        if (currentFragment == null && currentActivity == null)
            throw IllegalStateException("[VaccPass] Context is null for message request")

        when (request) {
            is SnackbarRequest -> {
                // TODO: display snackbar
            }
            is DialogRequest -> {
                val positiveListener =
                    request.positiveButton?.onClick?.let { DialogInterface.OnClickListener { _, _ -> request.positiveButton.onClick.invoke() } }
                val negativeListener =
                    request.negativeButton?.onClick?.let { DialogInterface.OnClickListener { _, _ -> request.negativeButton.onClick.invoke() } }

                displayDialog(
                    title = request.title,
                    msg = request.message,
                    positiveBtn = request.positiveButton?.caption,
                    positiveBtnListener = positiveListener,
                    negativeBtn = request.negativeButton?.caption,
                    negativeBtnListener = negativeListener
                )
            }
        }
    }

    override fun dispose() {
       disposables.dispose()
    }

    override fun isDisposed(): Boolean {
       return disposables.isDisposed
    }
}