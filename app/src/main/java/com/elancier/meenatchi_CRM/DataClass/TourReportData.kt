package com.elancier.meenatchi_CRM.DataClass

typealias TourReportData = ArrayList<TourReportDatum>

data class TourReportDatum (
    val date: String? = null,
    val tripId: String? = null,
    val name: String? = null,
    val area: String? = null,
    val from: String? = null,
    val to: String? = null,
    val target: String? = null,
    val budget: String? = null,
    val download: String? = null
)
