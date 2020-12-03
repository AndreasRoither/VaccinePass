package com.mobilehealthsports.vaccinepass.business.database.extension

import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.database.persistence.DbUser
import com.mobilehealthsports.vaccinepass.business.database.persistence.DbVaccine
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.business.models.Vaccine

/***********************
 * USER
 ************************/
fun DbUser.toUser() = User(
    uid = uid,
    firstName = firstName,
    lastName = lastName,
    bloodType = bloodType,
    birthDay = birthDay,
    weight = weight,
    height = height,
    themeColor = themeColor
)

fun User.toDb() = DbUser(
    uid,
    firstName = null,
    lastName = null,
    bloodType = null,
    birthDay = null,
    weight = null,
    height = null,
    themeColor = R.color.app_primary
)

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
    refreshDate = refreshDate
)

fun Vaccine.toDb() = DbVaccine(
    uid,
    name,
    company = null,
    indication = null,
    targetGroup = null,
    note = null,
    adjuvans = null,
    thiomersal = null,
    refreshRecommendation = null,
    refreshDate = null
)