package com.elancier.meenatchi_CRM.Adapers

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.elancier.meenatchi_CRM.R
import com.elancier.meenatchi_CRM.retrofit.ExecutiveData


class ExecutiveAdapter(
        private val context: Activity, //to store the list of countries
        private val exe: ExecutiveData) : ArrayAdapter<Any>(context, R.layout.dropdown_item,
    (exe as List<Any>?)!!
) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        //val inflater = context.layoutInflater
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.dropdown_item, null, true)
        val Fid = rowView.findViewById<TextView>(android.R.id.text1)
        val e = exe[position]
        Fid.setText(e.first_name+" "+e.last_name+"-"+e.mobile)

        return rowView

    }
}