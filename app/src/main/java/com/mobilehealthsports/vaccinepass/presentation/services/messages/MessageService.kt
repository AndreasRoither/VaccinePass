package com.mobilehealthsports.vaccinepass.presentation.services.messages

import android.content.DialogInterface
import android.view.View
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Service to send messages to the current screen
 * Supports dialogs and toast messages
 */
interface MessageService: Disposable {
    fun displayToastMessage(msgId: Int)
    fun displayToastMessage(msg: String)

    fun displaySnackbar(view: View, msgId: Int, btnMsg: Int?, btnListener: DialogInterface.OnClickListener?)
    fun displaySnackbar(view: View, msg: String, btnMsg: String?, btnListener: DialogInterface.OnClickListener?)

    fun displayDialog(
        titleId: Int, msgId: Int,
        positiveBtnId: Int, positiveBtnListener: DialogInterface.OnClickListener?,
        negativeBtnId: Int, negativeBtnListener: DialogInterface.OnClickListener?
    )
    fun displayDialog(
        title: String?, msg: String,
        positiveBtn: String?, positiveBtnListener: DialogInterface.OnClickListener?,
        negativeBtn: String?, negativeBtnListener: DialogInterface.OnClickListener?
    )

    fun subscribeToRequests(service: ServiceRequest<MessageRequest>)
    fun executeRequest(request: MessageRequest)
}