package com.elancier.meenatchi_CRM

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.elancier.meenatchi_CRM.DataClass.OrderDetail
import com.elancier.meenatchi_CRM.retrofit.Appconstants
import com.elancier.meenatchi_CRM.retrofit.ApproveUtils
import com.elancier.meenatchi_CRM.retrofit.Resp_trip
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FragmentCurrentTrips : Fragment() {
    internal lateinit var editor: SharedPreferences.Editor
    internal lateinit var pref: SharedPreferences
    var CentresArrays = ArrayList<OrderDetail>()
    lateinit var adp: MyBranchAdap
    lateinit var adp1: MyBranchAdap
    lateinit var click: MyBranchAdap.OnItemClickListener

    var textView23:TextView?=null
    var recyclerView :RecyclerView?=null
    var shimmer_view_container :ShimmerFrameLayout?=null
    lateinit var swiperefresh : SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.upcoming_fragment, container, false)
        recyclerView= root.findViewById<View>(R.id.list_item) as RecyclerView
        textView23= root.findViewById<View>(R.id.textView23) as TextView
        shimmer_view_container= root.findViewById<View>(R.id.shimmer_view_container) as ShimmerFrameLayout
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setHasFixedSize(true)
        pref = requireActivity().getSharedPreferences("MyPref", 0)
        editor = pref.edit()
        //var type = pref.getString("id", "")


        click = object : MyBranchAdap.OnItemClickListener {
            override fun OnItemClick(view: View, position: Int, viewType: Int) {

            }

        }
        swiperefresh = root.findViewById(R.id.swiperefresh)
        swiperefresh.setOnRefreshListener {
            Log.i("refresh", "onRefresh called from SwipeRefreshLayout")
            shimmer_view_container!!.visibility=View.VISIBLE
            shimmer_view_container!!.startShimmer()
            onResume()
            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        if (Appconstants.net_status(requireActivity())) {
            textView23!!.visibility=View.GONE
            CentresArrays.clear()
            val call = ApproveUtils.Get.gettrips(pref.getString("mobile","").toString())
            call.enqueue(object : Callback<Resp_trip> {
                override fun onResponse(call: Call<Resp_trip>, response: Response<Resp_trip>) {
                    Log.e("areacode responce", response.toString())
                    if (response.isSuccessful()) {
                        val example = response.body() as Resp_trip
                        println(example)
                        if (example.getStatus() == "Success") {
                            textView23!!.visibility=View.GONE
                            var otpval=example.getcurrenttrip()
                            for(i in 0 until otpval!!.size) {
                                val data= OrderDetail()
                                data.area = otpval[i].area
                                data.id = otpval[i].id.toString()
                                data.tid = otpval[i].tid.toString()
                                data.user = otpval[i].user
                                data.area = otpval[i].area
                                data.plan_date = otpval[i].planDate
                                data.target_value=otpval[i].targetValue
                                data.from_date=otpval[i].fromDate
                                data.to_date=otpval[i].toDate
                                data.days=otpval[i].days
                                data.ex_customers=otpval[i].exCustomers
                                data.new_customers=otpval[i].newCustomers
                                data.budget=otpval[i].budget
                                data.customers=otpval[i].customers
                                data.status=otpval[i].status.toString()
                                data.created_at=otpval[i].createdAt
                                data.updated_at=otpval[i].updatedAt
                                //db.CentreInsert(data)
                                CentresArrays.add(data)

                            }
                            adp = MyBranchAdap(
                                CentresArrays,
                                activity!!,
                                click
                            )
                            recyclerView!!.adapter = adp
                            shimmer_view_container!!.stopShimmer()
                            shimmer_view_container!!.visibility=View.GONE

                            if(CentresArrays.isEmpty()){
                                textView23!!.visibility=View.VISIBLE

                            }

                        } else {

                            shimmer_view_container!!.visibility=View.GONE
                            textView23!!.visibility=View.VISIBLE
                            shimmer_view_container!!.stopShimmer()

                        }
                    }
                    else{
                        shimmer_view_container!!.visibility=View.GONE
                        textView23!!.visibility=View.VISIBLE
                        shimmer_view_container!!.stopShimmer()
                    }
                    swiperefresh.isRefreshing=false
                }

                override fun onFailure(call: Call<Resp_trip>, t: Throwable) {
                    Log.e("areacode Fail response", t.toString())
                    if (t.toString().contains("time")) {
                        Toast.makeText(
                            activity!!,
                            "Poor network connection",
                            Toast.LENGTH_LONG
                        ).show()
                        shimmer_view_container!!.visibility=View.GONE
                        shimmer_view_container!!.stopShimmer()


                    }
                    swiperefresh.isRefreshing=false
                }
            })
        }
        else{
            Toast.makeText(
                requireActivity(),
                "You're offline",
                Toast.LENGTH_LONG
            ).show()
            shimmer_view_container!!.visibility=View.GONE
            swiperefresh.isRefreshing=false
        }

        /* memberslist.setLayoutManager(LinearLayoutManager(this));
        adp = MyBranchAdap(CentresArrays, this@Branch_List, click)
        memberslist.adapter = adp

        memberslist.setLayoutManager(LinearLayoutManager(this));
        adp1 = MyBranchAdap(CentresArraysdup, this@Branch_List, click)
        memberslist.adapter = adp1*/

    }
    }

