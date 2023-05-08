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

class ExpenditurePlanAdapter(val context: Context, private val items: JSONArray, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    interface OnItemClickListener {
        fun OnItemExpenditurePlanClick(view: View, position:Int, viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.expanditure_adapter, viewGroup, false)
        return ItemViewHolder(productView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder
                val obj = items.getJSONObject(position)
                //["expnediture_date":"","expenditure_city":"","travel":"","food":"","stay":"","others":"","total":""]}
                val expnediture_date = if (obj.has("expnediture_date")) obj.get("expnediture_date").toString() else ""
                val expenditure_city = if (obj.has("expenditure_city")) obj.get("expenditure_city").toString() else ""
                val travel = if (obj.has("travel")) obj.get("travel").toString() else ""
                val food = if (obj.has("food")) obj.get("food").toString() else ""
                val stay = if (obj.has("stay")) obj.get("stay").toString() else ""
                val others = if (obj.has("others")) obj.get("others").toString() else ""
                val total = if (obj.has("total")) obj.get("total").toString() else ""
                //holder.nm.setText(CentreArrays[position].cname.toString())//(CentreArrays.getJSONObject(position).getString("cname"))
                holder.city.setText(expenditure_city)
                holder.date_txt.setText(expnediture_date)
                holder.stay.setText(stay)
                holder.food.setText(food)
                holder.travel.setText(travel)
                holder.other.setText(others)
                holder.total.setText(total)

                holder.delete.setOnClickListener {
                    (context as AdvanceRequest).delete_expenditure(position)
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

        internal lateinit var city: TextView
        internal lateinit var date_txt: TextView
        internal lateinit var stay: TextView
        internal lateinit var food: TextView
        internal lateinit var travel: TextView
        internal lateinit var other: TextView
        internal lateinit var total: TextView
        internal lateinit var delete: ImageButton

        init {

            city = rowView.findViewById(R.id.city) as TextView
            date_txt = rowView.findViewById(R.id.date_txt) as TextView
            stay = rowView.findViewById(R.id.stay) as TextView
            food = rowView.findViewById(R.id.food) as TextView
            travel = rowView.findViewById(R.id.travel) as TextView
            other = rowView.findViewById(R.id.other) as TextView
            total = rowView.findViewById(R.id.total) as TextView
            delete = rowView.findViewById(R.id.delete) as ImageButton

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            try {
                listener.OnItemExpenditurePlanClick(
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