package com.example.doctor_app.business.database.extension

import com.example.doctor_app.business.database.persistence.DbVaccine
import com.example.doctor_app.business.models.Vaccine


/***********************
 * VACCINE
 ************************/
fun DbVaccine.toVaccine() = Vaccine(
    uid = uid,
    name = name,
    company = company,
    indication = indication,
    targetGroup = targetGroup,
    note = note,
    adjuvans = adjuvans,
    thiomersal = thiomersal,
    refreshRecommendation = refreshRecommendation,
    active = active
)

fun Vaccine.toDb() = DbVaccine(
    uid = uid,
    name = name,
    company = company,
    indication = indication,
    targetGroup = targetGroup,
    note = note,
    adjuvans = adjuvans,
    thiomersal = thiomersal,
    refreshRecommendation = refreshRecommendation,
    active = active
)