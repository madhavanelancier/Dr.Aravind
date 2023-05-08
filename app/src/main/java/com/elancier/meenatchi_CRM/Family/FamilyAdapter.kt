package com.elancier.meenatchi_CRM.Family

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.elancier.meenatchi_CRM.DataClasses.FamilyMember
import com.elancier.meenatchi_CRM.R


class FamilyAdapter(
        private val context: Activity, //to store the list of countries
        private val Family: ArrayList<FamilyMember>) : ArrayAdapter<Any>(context, R.layout.family_list_adapter,
    (Family as List<Any>?)!!
) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        //val inflater = context.layoutInflater
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.family_list_adapter, null, true)
        val Fid = rowView.findViewById<TextView>(R.id.textView10)
        val Name = rowView.findViewById<TextView>(R.id.textView3)
        val Relation = rowView.findViewById<TextView>(R.id.textView4)
        val Age = rowView.findViewById<TextView>(R.id.textView5)
        val Occuption = rowView.findViewById<TextView>(R.id.textView6)
        val Income = rowView.findViewById<TextView>(R.id.textView7)

        Fid.setText(Family[position].Fid)
        Name.setText(Family[position].Name)
        Relation.setText(Family[position].Relation)
        Age.setText(Family[position].Age)
        Occuption.setText(Family[position].Occuption)
        Income.setText(Family[position].Income)
        Occuption.isSelected=true
        Income.isSelected=true

        return rowView

    }
}