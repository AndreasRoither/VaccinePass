package com.example.doctor_app.business.repository

import com.example.doctor_app.business.models.Vaccine
import com.example.doctor_app.business.database.AppDatabase
import com.example.doctor_app.business.database.extension.toDb
import com.example.doctor_app.business.database.extension.toVaccine
import java.util.ArrayList

class VaccineRepositoryImpl(private val database: AppDatabase?) : VaccineRepository {
    override suspend fun getVaccine(id: Long): Vaccine? {
        return database!!.vaccineDao().loadAllByIds(longArrayOf(id)).blockingFirst().firstOrNull()
            ?.toVaccine()
    }

    override suspend fun getAllVaccines(): List<Vaccine> {
        val vaccines = ArrayList<Vaccine>();
        vaccines.add( Vaccine(
                1,
                "DTaP-IPV-Vakzine SS",
                "AJ Vaccines A/S",
                "Di, Tet, Pert, Polio  (Grundimmunisierung) ",
                "Kinder ab 2 Monaten",
                "",
                "Aluminiumhydroxid ",
                "Nein",
                "",
                false))
        vaccines.add(Vaccine(
                2,
                "dT-reduct \"Merieux\"",
                "Sanofi Pasteur Europe",
                "Di, Tet  (Grundimmunisierung und Auffrischung)",
                "Ab 6 Jahren",
                "",
                "Aluminiumhydroxid ",
                "Nein",
                "",
                false))
        vaccines.add(Vaccine(
                3,
                "Dukoral",
                "Valneva Sweden AB",
                "Cholera  (Grundimmunisierung und Auffrischung)",
                "Kinder ab 2 Jahren und Erwachsene",
                "Orale Verabreichung",
                "",
                "Nein",
                "",
                false))
        vaccines.add(Vaccine(
                4,
                "Efluelda",
                "Sanofi Pasteur",
                "Influenza",
                "Ab 65 Jahren",
                "",
                "",
                "Nein",
                "",
                false))
        vaccines.add(Vaccine(
                5,
                "Encepur 0,5 ml",
                "GSK Vaccines GmbH",
                "FSME  (Grundimmunisierung und Auffrischung",
                "Ab 12 Jahren",
                "",
                "Aluminiumhydroxid",
                "Nein",
                "",
                false))
        vaccines.add(Vaccine(
                6,
                "Encepur 0,25 ml für Kinder",
                "GSK Vaccines GmbH",
                "FSME  (Grundimmunisierung und Auffrischung",
                "Kinder von 1 bis 12 Jahren",
                "",
                "Aluminiumhydroxid",
                "Nein",
                "",
                false))
        vaccines.add(Vaccine(
                7,
                "Engerix B 20 Mikrogramm",
                "GSK Pharma GmbH",
                "Hepatitis B",
                "Ab 15 Jahren",
                "",
                "Aluminiumhydroxid",
                "Nein",
                "",
                false))
        vaccines.add( Vaccine( 8, "Engerix B 10 Mikrogramm", "GSK Pharma GmbH", "Hepatitis B", "Neugeborene bis 15 Jahre", "", "Aluminiumhydroxid ", "Nein", "", false))
        vaccines.add( Vaccine( 9, "Fendrix", "GSK Biologicals S.A.", "Hepatitis B", "Ab 15 Jahren", "Anwendung bei Niereninsuffizienz", "AS04C", "Nein", "", false))
        vaccines.add( Vaccine( 10, "Fluad", "Seqirus S.r.l.", "Influenza", "Ab 65 Jahre", "", "MF59C.1", "Nein", "", false))
        vaccines.add( Vaccine( 11, "Fluad Tetra", "Seqirus Netherl. B.V.", "Influenza", "Ab 65 Jahren", "", "MF59C.1", "Nein", "", false))
        vaccines.add( Vaccine( 12, "Fluarix Tetra", "GSK Pharma GmbH", "Influenza", "Ab 6 Monaten", "", "", "Nein", "", false))
        vaccines.add( Vaccine( 13, "Flucelvax Tetra", "Seqirus Netherl. B.V.", "Influenza", "Ab 9 Jahren", "Virus in der Zellkultur gezüchtet; frei von Hühnereiweiß", "", "Nein", "", false))
        vaccines.add( Vaccine( 14, "Fluenz Tetra", "AstraZeneca AB", "Influenza", "Kinder und Jugendliche von 2 bis 18 Jahren", "Intranasale Applikation, Lebendimpfstoff, Kühlkettenpflicht", "", "Nein", "", false))

        return vaccines
    }

    override suspend fun getVaccineByName(name: String): Vaccine? {
        return database!!.vaccineDao().findByName(name).blockingGet()?.toVaccine()
    }

    override suspend fun insertVaccine(vaccine: Vaccine): Long? {
        return database!!.vaccineDao().insertAll(vaccine.toDb()).firstOrNull()
    }

    override suspend fun deleteVaccine(vaccine: Vaccine) {
        database!!.vaccineDao().delete(vaccine.toDb())
    }
}