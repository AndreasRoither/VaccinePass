package com.mobilehealthsports.vaccinepass.presentation.services.messages

sealed class MessageRequest
class ButtonModel(val caption: String, val onClick: (() -> Unit)? = null)

class SnackbarRequest(val message: String) : MessageRequest()

class DialogRequest(
    val title: String?,
    val message: String,
    val positiveButton: ButtonModel? = null,
    val negativeButton: ButtonModel? = null,
    val neutralButton: ButtonModel? = null
) : MessageRequest()

