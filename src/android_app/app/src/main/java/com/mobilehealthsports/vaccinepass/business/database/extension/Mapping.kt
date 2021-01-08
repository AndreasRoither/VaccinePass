package com.mobilehealthsports.vaccinepass.business.database.extension

import com.mobilehealthsports.vaccinepass.business.database.persistence.*
import com.mobilehealthsports.vaccinepass.business.models.*

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
    themeColor = themeColor,
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

/***********************
 * APPOINTMENT
 ************************/
fun DbAppointment.toAppointment() = Appointment(
    uid = uid,
    title = title,
    place = place,
    appointment_date = appointment_date,
    reminder = reminder,
    reminder_date = reminder_date
)

fun Appointment.toDb() = DbAppointment(
    uid = uid,
    title = title,
    place = place,
    appointment_date = appointment_date,
    reminder = reminder,
    reminder_date = reminder_date
)