package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Edit_Datum {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("unit_code")
    @Expose
    var unitCode: String? = null
    @SerializedName("village_id")
    @Expose
    var villageId: String? = null
    @SerializedName("familyhead_name")
    @Expose
    var familyheadName: String? = null
    @SerializedName("familyhead_age")
    @Expose
    var familyheadAge: String? = null
    @SerializedName("other_work")
    @Expose
    var other_work: String? = null
    @SerializedName("familyhead_occupation")
    @Expose
    var familyheadOccupation: String? = null
    @SerializedName("familyhead_income")
    @Expose
    var familyheadIncome: String? = null
    @SerializedName("familyhead_salary")
    @Expose
    var familyheadSalary: String? = null
    @SerializedName("coverted_familyhead_salary")
    @Expose
    var covertedFamilyheadSalary: String? = null
    @SerializedName("health_issue")
    @Expose
    var healthIssue: String? = null
    @SerializedName("healthissue_description")
    @Expose
    var healthissueDescription: String? = null
    @SerializedName("house")
    @Expose
    var house: String? = null
    @SerializedName("house_condition")
    @Expose
    var houseCondition: String? = null
    @SerializedName("house_image")
    @Expose
    var houseImage: List<HouseImage>? = null
    @SerializedName("date_installed")
    @Expose
    var dateInstalled: String? = null
    @SerializedName("fish")
    @Expose
    var fish: String? = null
    @SerializedName("plant_seeded")
    @Expose
    var plantSeeded: List<PlantSeeded>? = null
    @SerializedName("family_image")
    @Expose
    var familyImage: List<FamilyImage>? = null
    @SerializedName("unitinstall_image")
    @Expose
    var unitinstallImage: List<UnitinstallImage>? = null
    @SerializedName("familly")
    @Expose
    var familly: List<Edit_familly>? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

}