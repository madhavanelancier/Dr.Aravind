package com.elancier.meenatchi_CRM.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Edit_familly {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("age")
    @Expose
    var age: String? = null
    @SerializedName("relation")
    @Expose
    var relation: String? = null
    @SerializedName("school")
    @Expose
    var school: String? = null
    @SerializedName("occupation")
    @Expose
    var occupation: String? = null
    @SerializedName("monthly_income")
    @Expose
    var monthlyIncome: String? = null
    @SerializedName("coverted_salary")
    @Expose
    var covertedSalary: String? = null
    @SerializedName("health_issue")
    @Expose
    var healthIssue: String? = null
    @SerializedName("health_issue_description")
    @Expose
    var healthIssueDescription: String? = null
    @SerializedName("future_goal")
    @Expose
    var futureGoal: String? = null

}