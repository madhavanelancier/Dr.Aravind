package com.elancier.meenatchi_CRM.retrofit

import android.content.SharedPreferences
import com.elancier.meenatchi_CRM.DataClass.TourReportData
import com.google.gson.JsonObject
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface usr {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor


    @GET()
    fun gethar(@Url st : String): Call<Resp_harvest>

    @GET()
    fun getMem(@Url st : String): Call<Resp_Home>

    @GET()
    fun getMemedit(@Url st : String): Call<Resp_Edit>

    @GET()
    fun getMem2(@Url st : String): Call<Resp>

    @GET()
    fun getexplimit(@Url st : String): Call<Resp_otps>

    @GET()
    fun getMem2rep(@Url st : String): Call<Login_Resp>

    @GET()
    fun getMem3(@Url st : String): Call<Resp>

    @GET()
    fun deleteexp(@Url st : String): Call<Resp_otp>

    @GET()
    fun geteodsatte(@Url st : String): Call<Resp_otp>


    @GET()
    fun getMem4(@Url st : String): Call<Resp_otp>

    @FormUrlEncoded
    @POST("area_status")
    fun getMemer2(@Field("area_id") st : String,@Field("status")sts:String): Call<Resp_otp>

    @FormUrlEncoded
    @POST("userState")
    fun getstate(@Field("userid") st : String): Call<Resp_otp>

    @FormUrlEncoded
    @POST("village_status")
    fun getbr2(@Field("village_id") st : String,@Field("status")sts:String): Call<Resp_dup>

    @FormUrlEncoded
    @POST("family_status")
    fun getbrfamily2(@Field("family_id") st : String,@Field("status")sts:String): Call<Resp_dup>

    @FormUrlEncoded
    @POST("edit_profile")
    fun geteditfamily2(@Field("name") st : String): Call<Resp_dup>


    @FormUrlEncoded
    @POST("staff_status")
    fun getstaff2(@Field("staff_id") st : String,@Field("status")sts:String): Call<Resp_otp>

    @FormUrlEncoded
    @POST("centre_approve")
    fun getcenapprove2(@Field("centre_id") st : String,@Field("status")sts:String): Call<Resp_otp>

    @FormUrlEncoded
    @POST("member_approve")
    fun memapprove(@Field("member_id") st : String,@Field("status")sts:String): Call<Resp_otp>

    @POST("brands")
    fun getbrands(): Call<Resp_trip>


    @POST("price_list")
    fun getprice_list(): Call<Resp_trip>

    @POST("product_stocks")
    fun product_stocks(): Call<Resp_trip>

    @POST("discount")
    fun getdiscount(): Call<Resp_trip>


    @FormUrlEncoded
    @POST("products")
    fun getproducts(@Field("brand")state : String): Call<Resp_trip>

    @FormUrlEncoded
    @POST("device_status")
    fun getdevapprove2(@Field("staff_id") st : String,@Field("status")sts:String): Call<Resp_otp>

    @FormUrlEncoded
    @POST("income_status")
    fun getincomeapprove2(@Field("income_id") st : String,@Field("status")sts:String): Call<Resp_otp>

    @FormUrlEncoded
    @POST("expense_status")
    fun getexpapprove2(@Field("expense_id") st : String,@Field("status")sts:String): Call<Resp_otp>

    @FormUrlEncoded
    @POST("search.php")
    fun search(@Field("religion")member_id : String,
                  @Field("gender")centre_id : String,
                  @Field("agef")member_name : String,
                  @Field("aget")amount : String,
                  @Field("caste")paid_amount : String,
                  @Field("marital")status : String,
               @Field("edu")edu : String,
               @Field("start")start : String,
               @Field("limit")limit : String): Call<Search_model>


    @FormUrlEncoded
    @POST("denomination_status")
    fun den_close(@Field("denomination_id")member_id : String,
                  @Field("status")centre_id : String): Call<Resp_otp>


    @FormUrlEncoded
    @POST("sendotp")
    fun getLogin(
        @Field("mobile")username : String

    ): Call<Resp_otp>

    @POST("login")
    fun getlogin(
        @Body fam:JsonObject

    ): Call<Resp_otp>

    @POST("dashboard")
    fun getdashboard(
        @Body fam:JsonObject

    ): Call<Resp_otp>



    @FormUrlEncoded
    @POST("geolocation")
    fun getgeo(
        @Field("trip_id")trip_id : String,
        @Field("user_id")user_id : String,
        @Field("lat")lat : String,
        @Field("lon")lon : String

    ): Call<Resp_otp>

    @FormUrlEncoded
    @POST("request_list")
    fun getreq(
        @Field("tid")tid : String

    ): Call<Resp_trip>

    @FormUrlEncoded
    @POST("sales_list")
    fun getRequestList(@Field("cus_id")tid : String,@Field("enter_by")enter_by : String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("collected_list")
    fun getcollected_list(@Field("cus_id")tid : String,@Field("enter_by")enter_by : String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("credit_list")
    fun credit_list(@Field("cus_id")tid : String,@Field("enter_by")enter_by : String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("orders_list")
    fun getorders_list(@Field("cus_id")tid : String,@Field("enter_by")enter_by : String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("expense_list")
    fun getexp(
        @Field("tid")username : String

    ): Call<Resp_trip>

    @FormUrlEncoded
    @POST("customer_entry")
    fun getentry(
        @Field("tid")username : String,
        @Field("customer_id")customer_id:String,
        @Field("notes")notes:String,
        @Field("order_details")order_details:String,
        @Field("order_value")order_value:String,
        @Field("image")image:String,
        @Field("image1")image1:String,
        @Field("user")user:String

    ): Call<Resp_otp>

    @FormUrlEncoded
    @POST("add_request")
    fun getreqentry(
        @Field("tid")username : String,
        @Field("user")customer_id:String,
        @Field("request_type")notes:String,
        @Field("name")order_details:String,
        @Field("description")order_value:String,
        @Field("image")image:String,
        @Field("image1")image1:String,
        @Field("amount")user:String

    ): Call<Resp_otp>


    @FormUrlEncoded
    @POST("doctor/add")
    fun getcustentry(
        @Field("name")username : String,
        @Field("employee")customer_id:String,
        @Field("specialization")notes:String,
        @Field("clinic")order_details:String,
        @Field("address")order_value:String,
        @Field("city")state:String,
        @Field("doctor_contact_number")image:String,
        @Field("hospital_contact_number")contact_person:String,
        @Field("location_pin")designation:String,
        @Field("lat")image1:String,
        @Field("long")image2:String,
        @Field("image3")image3:String,
        @Field("image4")image4:String,
        @Field("image5")image5:String

    ): Call<Resp_otp>

    @POST("doctor/add")
    fun addDoctor(@Body file: RequestBody?): Call<ResponseBody?>?

    @POST("patient/add")
    fun addPatient( @Body fam:JsonObject): Call<ResponseBody?>?

    @POST("meeting/add")
    fun addMeeting( @Body fam:JsonObject): Call<ResponseBody?>?

    @POST("meeting/edit/{api_key}")
    fun editMeeting( @Path("api_key")trip : String): Call<ResponseBody?>?
    @FormUrlEncoded
    @POST("profile_update")
    fun getprofile(
        @Field("mobile")username : String,
        @Field("first_name")customer_id:String,
        @Field("last_name")notes:String,
        @Field("email")order_details:String

    ): Call<Resp_otps>


    @FormUrlEncoded
    @POST("add_expense")
    fun getexpentry(
        @Field("tid")username : String,
        @Field("user")customer_id:String,
        @Field("expense_type")notes:String,
        @Field("name")order_details:String,
        @Field("description")order_value:String,
        @Field("image")image:String,
        @Field("image1")image1:String,
        @Field("amount")user:String

    ): Call<Resp_otp>

    @FormUrlEncoded
    @POST("customer_entry_update")
    fun getentry_update(
        @Field("tid")username : String,
        @Field("customer_id")customer_id:String,
        @Field("notes")notes:String,
        @Field("order_details")order_details:String,
        @Field("order_value")order_value:String,
        @Field("image")image:String,
        @Field("image1")image1:String,
        @Field("user")user:String,
        @Field("id")id:String

    ): Call<Resp_otp>

    @FormUrlEncoded
    @POST("dashboard")
    fun getdash(
        @Field("user_id")username : String

    ): Call<Resp_otp>


    @FormUrlEncoded
    @POST("forgot_pwd.php")
    fun forgot(
        @Field("mobile")username : String,

        @Field("religion")religion : String


    ): Call<Login_Resp>

    @FormUrlEncoded
    @POST("home_header.php")
    fun getheader(
                 @Field("religion")username : String


    ): Call<Resp>
    @FormUrlEncoded
    @POST("verifyotp")
    fun getotp(@Field("mobile")centre_id : String,
                 @Field("otp")amount : String,
               @Field("user_otp")user_otp : String,
               @Field("device_id")device_id : String,
               @Field("token")token : String
                ): Call<Resp_otps>

    //{"centre_id":"67","amount":"2800","paid_amount":"0","status":"Whole OD","staff_id":"177"}
    @FormUrlEncoded
    @POST("member_pay")
    fun MemberPay(@Field("member_id")member_id : String,
                  @Field("centre_id")centre_id : String,
                 @Field("member_name")member_name : String,
                 @Field("amount")amount : String,
                 @Field("paid_amount")paid_amount : String,
                 @Field("status")status : String,
                 @Field("attendance")attendance : String,
                 @Field("partial_image")partial_image : String): Call<Resp>
//http://teamdev.co.in/domdox/api/member_pay
    //{"member_id":"3","member_name":"rahul","amount":"400","paid_amount":"400","status":"Paid","attendance":"Present","partial_image":""}
    @FormUrlEncoded
    @POST("feedback")
    fun LateFeed(@Field("staff_id")staff_id : String,
                  @Field("centre_id")centre_id : String,
                  @Field("reason")reason : String): Call<Resp>

    //{"staff_id":"17","centre_id":"1","reason":"Reason"}

    @FormUrlEncoded
    @POST("newmember")
    fun Verification(@Field("staff_id")staff_id : String,
                     @Field("area_code")area_code : String,
                     @Field("centre_code")centre_code : String,
                     @Field("member_position")member_position : String,
                     @Field("member_name")member_name : String,
                     @Field("member_mobile")member_mobile : String,
                     @Field("dob_age")dob_age : String,
                     @Field("member_rationcard")member_rationcard : String,
                     @Field("member_voterid")member_voterid : String,
                     @Field("member_aadhar")member_aadhar : String,
                     @Field("religion")religion : String,
                     @Field("community")community : String,
                     @Field("loan_amt")loan_amt : String,
                     @Field("income")income : String,
                     @Field("nominee_name")nominee_name : String,
                     @Field("nominee_relationship")nominee_relationship : String,
                     @Field("nominee_dob_age")nominee_dob_age : String,
                     @Field("nominee_voterid")nominee_voterid : String,
                     @Field("nominee_aadhar")nominee_aadhar : String,
                     @Field("business")business : String,
                     @Field("business_year")business_year : String,
                     @Field("member_placement")member_placement : String,
                     @Field("member_birthplace")member_birthplace : String,
                     @Field("member_address")member_address : String,
                     @Field("member_communication_address")member_communication_address : String,
                     @Field("member_image")member_image : String,
                     @Field("member_nominee_image")member_nominee_image : String,
                     @Field("member_signature")member_signature : String,
                     @Field("nominee_signature")nominee_signature : String
                     ): Call<Resp>

    /*staff_id
area_code
centre_code
member_position
member_name
member_mobile
dob_age
member_rationcard
member_voterid
member_aadhar
religion
community
loan_amt
income
nominee_name
nominee_relationship
nominee_dob_age
nominee_voterid
nominee_aadhar
business
business_year
member_placement
member_birthplace
member_address
member_communication_address
member_image
member_nominee_image
member_signature
nominee_signature*/

    @FormUrlEncoded
    @POST("addfamily")
    fun Family(@Body fam : JSONObject): Call<Resp>


    @FormUrlEncoded
    @POST("leave")
    fun LeaveApply(@Field("staff_id")staff_id : String,
                 @Field("d_date")d_date : String,
                 @Field("leave_for")leave_for : String): Call<Resp>
/*{"staff_id":"17","d_date":"Today","leave_for":"Reason"}*/

    @GET("upcoming_trip")
    fun getvillage(): Call<Resp>

    @GET("request_type")
    fun getreqtype(): Call<Resp_trip>


    @GET("city/list")
    fun getcities():Call<Resp_trip>

    //@FormUrlEncoded
    @POST("add_request")
    @Headers("Content-Type:application/json")
    fun sendAdvanceRequest/*( @Field("user")user : String,@Field("tid")tid : String,
                            @Field("request_type")request_type : String,
                            @Field("amount")amount : String,
                            @Field("tour_plan")tour_plan : JSONArray,
                            @Field("expenditure_plan")expenditure_plan : JSONArray,):Call<Resp_trip>*/(@Body obj : JSONObject):Call<Resp_trip>

    @FormUrlEncoded
    @POST("add_expense")
    //@Headers("Content-Type:application/json")
    fun sendExpense(
        @Field("user")user : String,
        @Field("tid")tid : String,
        @Field("expense_type")expense_type : String,
        @Field("name")name : String,
        @Field("image")image : String,
        @Field("description")description : String,
        @Field("amount")amount : String,
        @Field("state")state : String,
        @Field("city")city : String,
        @Field("from")from : String,
        @Field("to")to : String,
        @Field("by")by : String,
        @Field("hotel_name")hotel_name : String,
    ):Call<Resp_trip>

    @POST("order")
    //@Headers("Content-Type:application/json")
    fun sendorder(
        @Body fam : JsonObject
    ):Call<Resp_trip>

    @POST("add_credit")
    //@Headers("Content-Type:application/json")
    fun add_credit(
        @Body fam : JsonObject
    ):Call<Resp_otp>

    @POST("customer_add")
    //@Headers("Content-Type:application/json")
    fun customer_add(
        @Body fam : JsonObject
    ):Call<Resp_trip>

    @FormUrlEncoded
    @POST("expenseCheck")
    //@Headers("Content-Type:application/json")
    fun checkExpense(
        @Field("user")user : String,
        @Field("trip")tid : String,
        @Field("expensetype")expense_type : String,
        @Field("amount")name : String,
        @Field("city")cities : String
    ):Call<Resp_trip>

    @FormUrlEncoded
    @POST("frontsheet_entry")
    //@Headers("Content-Type:application/json")
    fun sendEOD(
        @Field("user")user : String,
        @Field("state")state : String,
        @Field("city")city : String,
        @Field("start_time")start_time : String,
        @Field("exit_time")exit_time : String,
        @Field("planned")planned : String,
        @Field("executed")executed : String,
        @Field("turnover")turnover : String
    ):Call<Resp_trip>


    @GET("specialization/list")
    fun getspecialized(): Call<Resp_trip>

    @FormUrlEncoded
    @POST("frontsheet")
    fun getEOD(@Field("user")user:String,@Field("date")date:String): Call<ResponseBody>

    @GET("expense_type")
    fun getexptype(): Call<Resp_trip>

    @GET("cloudinary")
    fun getcloudinary(): Call<Resp_otps>

    @GET("states")
    fun getState(): Call<StateData>

    @GET("salesExecutive")
    fun getExecutives(): Call<ExecutiveData>

    @FormUrlEncoded
    @POST("upcoming_trip")
    fun gettrips(@Field("mobile")centre_id : String):Call<Resp_trip>



    @GET("patient/list/{api_key}")
    fun getPatients(@Path("api_key")trip : String):Call<Resp_trip>

    @GET("meeting/list/{api_key}/{from}/{to}")
    fun getMeetings(@Path("api_key")trip : String,@Path("from")from : String,@Path("to")to : String):Call<Resp_trip>

    @GET("doctor/list/{api_key}")
    fun getcustrips(@Path("api_key")api_key:String):Call<Resp_trip>

    @FormUrlEncoded
    @POST("reportdata")
    fun gettourreport(
        @Field("year")year : String,
        @Field("user")user : String,
        @Field("season_type")season_type : String,
        @Field("state")state : String
    ):Call<TourReportData>

    @FormUrlEncoded
    @POST("eodCustomer")
    fun eodCustomer(@Field("state")centre_id : String,@Field("city")city : String):Call<Resp_trip>

    @FormUrlEncoded
    @POST("feedback_list")
    fun getCustomersByCity(@Field("cus_id")city : String,@Field("enter_by")enter_by : String):Call<CustomersListByCity>

    @FormUrlEncoded
    @POST("customer_entry_list")
    fun getordertrips(@Field("tid")centre_id : String):Call<Resp_trip>


    @GET("maintenance")
    fun getMaintenance(): Call<Resp>

    @GET("area_list")
    fun getarea(): Call<Resp>

    @GET("branch_list")
    fun getbrlist(): Call<Resp>

    @GET("staff_list")
    fun getstafflist(): Call<Resp>

    @GET("get_bankstaff")
    fun getbkstafflist(): Call<Resp>

    @GET("get_bank")
    fun getbanklist(): Call<Resp>



    @GET("get_particulars")
    fun getpartilist(): Call<Resp>


    @GET("fo_feedbacks")
    fun getfeedlist(): Call<Resp>

    @GET("member_feedbacks")
    fun gememfeedlist(): Call<Resp>

    @GET("verify_list")
    fun getverifylist(): Call<Resp>

    @GET("device_list")
    fun getdevicelist(): Call<Resp>



    @GET("income_list")
    fun getinclist(): Call<Resp>

    @GET("expense_list")
    fun getexplist(): Call<Resp>

    @GET("incentive")
    fun getincenlist(): Call<Resp>

    @GET("member_list")
    fun getmemberlist(): Call<Resp>


    @GET("leave_list")
    fun getleavelist(): Call<Resp>

    @GET("notification_list")
    fun getnotilist(): Call<Resp>

    @GET("holiday_list")
    fun getholilist(): Call<Resp>

    @GET("holidays")
    fun getholilists(): Call<Resp>

    @GET("centre_list")
    fun getctrlist(): Call<Resp>

    @GET("savings_account")
    fun getsavingslist(): Call<Resp>

    @GET("savings_list")
    fun getsavingsaclist(): Call<Resp>

    @GET("get_branch")
    fun getbr(): Call<Resp>

    @POST("check_in_out")
    fun check_in( @Body fam:JsonObject):Call<Resp_otp>




    @FormUrlEncoded
    @POST("income_edit")
    fun incedit(@Field("income_id")staff_id : String,
                @Field("name")d_date : String): Call<Resp>


    @FormUrlEncoded
    @POST("expense_edit")
    fun expedit(@Field("expense_id")staff_id : String,
                @Field("name")d_date : String): Call<Resp>

    @FormUrlEncoded
    @POST("device_edit")
    fun eddevice(@Field("staff_id")staff_id : String,
                @Field("device_id")d_date : String,
                @Field("allowed_date")leave_for : String,
                 @Field("extra_time")extra_time : String): Call<Resp>


    @FormUrlEncoded
    @POST("notification_add")
    fun add_noti(@Field("title")staff_id : String,
                 @Field("message")d_date : String
                ): Call<Resp>


    @FormUrlEncoded
    @POST("holiday_add")
    fun add_holiday(@Field("holidays")staff_id : String,
                 @Field("alternate_date")d_date : String
    ): Call<Resp>


    @FormUrlEncoded
    @POST("holiday_delete")
    fun del_holiday(@Field("holidays")staff_id : String,
                    @Field("alternate_date")d_date : String
    ): Call<Resp>

    @FormUrlEncoded
    @POST("dailyincome_add")
    fun daily_inc(@Field("name")staff_id : String,
                    @Field("type")d_date : String,
                    @Field("amount")amount : String

                    ): Call<Resp>


    @FormUrlEncoded
    @POST("dailyexpense_add")
    fun daily_exp(@Field("name")staff_id : String,
                  @Field("type")d_date : String,
                  @Field("amount")amount : String

    ): Call<Resp>

    @POST("feedback_save")
    fun feedback_save( @Body fam:JsonObject
    ): Call<Resp_otp>


    @FormUrlEncoded
    @POST("add_incentive")
    fun add_incen(@Field("outstation")staff_id : String,
                  @Field("localstation")d_date : String,
                  @Field("from_range")amount : String,
                  @Field("to_range")to_range : String,
                  @Field("amount")amounts : String
                  ): Call<Resp>

    @FormUrlEncoded
    @POST("change_maintenance")
    fun ed_maintain(@Field("status")staff_id : String

    ): Call<Resp>



    @FormUrlEncoded
    @POST("add_family")
    fun bradd(@Field("village_id")staff_id : String,
                @Field("familyhead_name")d_date : String,
                @Field("familyhead_age")address1 : String,
                @Field("familyhead_occupation")address2 : String,
                @Field("familyhead_income")city : String,
                @Field("familyhead_salary")state : String,
                @Field("health_issue")pincode : String,
                @Field("healthissue_description")country : String,
                @Field("house")email : String,
                @Field("house_condition")mobile : String,
                @Field("house_image")landline : String,
                @Field("unit_code") unit_code:String,
                @Field("date_installed") date_installed:String,
                @Field("fish") fish:String,
                @Field("plant_seeded") plant_seeded:String,
                @Field("family_image") family_image:String,
                @Field("unitinstall_image") unitinstall_image:String): Call<Resp>

    @FormUrlEncoded
    @POST("bank_add")
    fun bankadd(@Field("d_date")staff_id : String,
              @Field("bank_name")d_date : String,
              @Field("type")address1 : String,
              @Field("particular")address2 : String,
              @Field("staff_id")city : String,
              @Field("cheque_num")state : String,
              @Field("amount")pincode : String
             ): Call<Resp>


    @FormUrlEncoded
    @POST("centre_add")
    fun centreadd(@Field("area_code")staff_id : String,
              @Field("centre_name")d_date : String,
              @Field("centre_code")address1 : String,
              @Field("assigned_staff")address2 : String,
              @Field("assigned_day")city : String,
              @Field("station")state : String,
              @Field("from_time")pincode : String,
              @Field("to_time")country : String
            ): Call<Resp>

    @FormUrlEncoded
    @POST("centre_edit")
    fun centreedit(@Field("centre_id")centre_id : String,
        @Field("area_code")staff_id : String,
                  @Field("centre_name")d_date : String,
                  @Field("centre_code")address1 : String,
                  @Field("assigned_staff")address2 : String,
                  @Field("assigned_day")city : String,
                  @Field("station")state : String,
                  @Field("from_time")pincode : String,
                  @Field("to_time")country : String
    ): Call<Resp>


    @FormUrlEncoded
    @POST("branch_edit")
    fun brupdate(@Field("branch_id")br_id : String,
               @Field("branch_name")staff_id : String,
              @Field("open_date")d_date : String,
              @Field("address1")address1 : String,
              @Field("address2")address2 : String,
              @Field("city")city : String,
              @Field("state")state : String,
              @Field("pincode")pincode : String,
              @Field("country")country : String,
              @Field("email")email : String,
              @Field("mobile")mobile : String,
              @Field("landline")landline : String): Call<Resp>

    @FormUrlEncoded
    @POST("area_edit")

    fun updatearea(@Field("area_id")area_id : String,
                @Field("branch")staff_id : String,
                @Field("area_name")d_date : String,
                @Field("area_code")leave_for : String,
                   @Field("short")short : String): Call<Resp>

    @FormUrlEncoded
    @POST("denomination")
    fun Pay_denom(
        @Field("staff_id")staff_id : String,
        @Field("centre_id")centre_id : String,
        @Field("group_image")group_image : String,
        @Field("grand_total")grand_total : String,
        @Field("denomination")denom : JSONArray,
        @Field("members") pay : JSONArray
                  ): Call<Resp>

    /*{"centre_id": "1","group_image": "","grand_total": "4000","denomination": [{"denominator_id": "1","denominator": "2000","count": "1","total": "2000"},{"denominator_id": "2","denominator": "2000","count": "1","total": "2000"}]}*/

    @FormUrlEncoded
    @POST("feedback")
    fun Feedbacks(@Field("staff_id")staff_id : String,
                 @Field("centre_id")centre_id : String,
                 @Field("member_id")member_id : String,
                 @Field("reason")reason : String): Call<Resp>
}
