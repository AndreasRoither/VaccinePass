package com.mobilehealthsports.vaccinepass.presentation.services.navigation

import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Service to navigate to another screen
 */
interface NavigationService : Disposable {
    fun subscribeToRequests(service: ServiceRequest<NavigationRequest>)
    fun executeRequest(request: NavigationRequest)
}