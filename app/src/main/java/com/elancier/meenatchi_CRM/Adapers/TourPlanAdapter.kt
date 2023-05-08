package com.elancier.meenatchi_CRM.Adapers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elancier.meenatchi_CRM.AdvanceRequest
import com.elancier.meenatchi_CRM.R
import org.json.JSONArray

class TourPlanAdapter(val context: Context, private val items: JSONArray, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    interface OnItemClickListener {
        fun OnItemTourPlanClick(view: View, position:Int, viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.plan_adapter, viewGroup, false)
        return ItemViewHolder(productView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder
                val obj = items.getJSONObject(position)
                val customer_name = if (obj.has("customer_name")) obj.get("customer_name").toString() else ""
                val date = if (obj.has("tour_date")) obj.get("tour_date").toString() else ""
                val day = if (obj.has("day")) obj.get("day").toString() else ""
                val city = if (obj.has("tour_city")) obj.get("tour_city").toString() else ""
                //holder.nm.setText(CentreArrays[position].cname.toString())//(CentreArrays.getJSONObject(position).getString("cname"))
                holder.cust_nm.setText(customer_name)
                holder.city.setText(date)
                holder.date_txt.setText(day)
                holder.day.setText(city)
                holder.delete.setOnClickListener {
                    (context as AdvanceRequest).delete_tour(position)
                }
            }
            else -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        //val recyclerViewItem = CentreArrays[position]
        /*if (recyclerViewItem instanceof Confirmmodel) {
            return ITEM_CONFIRM_VIEW_TYPE;
        }*/
        return ITEM_CONTENT_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return items.length()
    }

    inner class ItemViewHolder internal constructor(rowView: View) : RecyclerView.ViewHolder(rowView), View.OnClickListener {

        internal lateinit var cust_nm: TextView
        internal lateinit var city: TextView
        internal lateinit var date_txt: TextView
        internal lateinit var delete: ImageButton
        internal lateinit var day: TextView

        init {

            cust_nm = rowView.findViewById(R.id.cust_nm) as TextView
            city = rowView.findViewById(R.id.city) as TextView
            date_txt = rowView.findViewById(R.id.date_txt) as TextView
            day = rowView.findViewById(R.id.day) as TextView
            delete = rowView.findViewById(R.id.delete) as ImageButton

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            try {
                listener.OnItemTourPlanClick(
                        view, adapterPosition,
                        ITEM_CONTENT_VIEW_TYPE
                )
            } catch (e: Exception) {

            }
        }
    }
    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }
}