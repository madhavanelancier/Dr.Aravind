package com.elancier.meenatchi_CRM.Adapers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.elancier.meenatchi_CRM.R
import com.elancier.meenatchi_CRM.retrofit.Response_trip
import java.util.ArrayList

class StateCityAdapter(internal var context: Context, internal var resource: Int, internal var items: ArrayList<Response_trip>,val state : Boolean,val city : Boolean,val request : Boolean,) : BaseAdapter() {
    internal var mInflater: LayoutInflater

    init {

        mInflater = LayoutInflater.from(context)
    }


    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any? {
        return if (state) items[position].state_name else if (city) items[position].name else items[position].type
    }

    override fun getItemId(position: Int): Long {
        return items[position].id!!.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder = ViewHolder()
        var alertView: LinearLayout? = null
        if (convertView == null) {
            convertView = mInflater.inflate(resource, alertView, true)
            convertView!!.tag = holder
            alertView = convertView as LinearLayout?
        } else {
            alertView = convertView as LinearLayout?
        }
        holder.text = convertView.findViewById<View>(R.id.spinner_text) as TextView

        holder.text!!.text = if (state) items[position].state_name else if (city) items[position].name else items[position].type

        return alertView as View
    }


    private class ViewHolder {
        var text: TextView? = null
    }
}