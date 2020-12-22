package com.example.doctor_app.business.models

data class Vaccine(
    val uid: Long,
    val name: String,
    val company: String?,
    val indication: String?,
    val targetGroup: String?,
    val note: String?,
    val adjuvans: String?,
    val thiomersal: String?,
    val refreshRecommendation: String?,
    val active: Boolean
) {
}