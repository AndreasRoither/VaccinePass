package com.mobilehealthsports.vaccinepass.business.database.extension

import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.database.persistence.DbUser
import com.mobilehealthsports.vaccinepass.business.database.persistence.DbVaccination
import com.mobilehealthsports.vaccinepass.business.database.persistence.DbVaccine
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.business.models.Vaccination
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
    themeColor = themeColor,
    photoPath = photoPath
)

fun User.toDb() = DbUser(
    uid = uid,
    firstName = firstName,
    lastName = lastName,
    bloodType = bloodType,
    birthDay = birthDay,
    weight = weight,
    height = height,
    themeColor = R.color.colorPrimary,
    photoPath = photoPath
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

/***********************
 * USER VACCINATION
 ************************/

fun DbVaccination.toVaccination() = Vaccination(
    uid = uid,
    f_uid = f_uid,
    active = active,
    refreshDate = refreshDate,
    userId = userId,
    vaccinationDate = vaccinationDate,
    expiresIn = expiresIn,
    doctorId = doctorId,
    doctorName = doctorName,
    signature = signature
)

fun Vaccination.toDb() = DbVaccination(
    uid = uid,
    f_uid = f_uid,
    active = active,
    refreshDate = refreshDate,
    userId = userId,
    vaccinationDate = vaccinationDate,
    expiresIn = expiresIn,
    doctorId = doctorId,
    doctorName = doctorName,
    signature = signature
)