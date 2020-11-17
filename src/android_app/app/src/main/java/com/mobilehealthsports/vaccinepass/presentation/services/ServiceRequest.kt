package com.mobilehealthsports.vaccinepass.presentation.services

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Used to subscribe to requests from viewModels for message or navigation requests
 * The service subscribes to the requests observable and handles requests
 *
 * This allows a separation of logic from the viewModel that should actually be handled
 * in the parent activity/fragment while still allowing the viewModel to keep all of the relevant logic
 */
class ServiceRequest<TRequest> {
    val requests: Observable<TRequest>
        get() = requestSubjects

    private val requestSubjects: PublishSubject<TRequest> = PublishSubject.create()

    fun request(model: TRequest) {
        requestSubjects.onNext(model)
    }
}