package com.elancier.meenatchi_CRM.Adapers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elancier.meenatchi_CRM.DataClass.TourReportData
import com.elancier.meenatchi_CRM.R

class TourReportAdapter(val context: Context, private val items: TourReportData, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    interface OnItemClickListener {
        fun OnItemPDFClick(view: View, position:Int,file:String, viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.tour_report_item, viewGroup, false)
        return ItemViewHolder(productView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder
                val obj = items[position]
                val tripId = obj.tripId
                val name = obj.name
                val date = obj.from+" to "+obj.to
                val target = obj.target
                val budget = obj.budget
                //holder.nm.setText(CentreArrays[position].cname.toString())//(CentreArrays.getJSONObject(position).getString("cname"))
                holder.trpid.setText(tripId)
                holder.nm.setText(name)
                holder.dt.setText(date)
                holder.tar.setText(target)
                holder.bud.setText(budget)
                holder.pdf.setOnClickListener {
                    listener.OnItemPDFClick(it, position, obj.download.toString(),ITEM_CONTENT_VIEW_TYPE)
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
        return items.size
    }

    inner class ItemViewHolder internal constructor(rowView: View) : RecyclerView.ViewHolder(rowView), View.OnClickListener {

        internal lateinit var trpid: TextView
        internal lateinit var nm: TextView
        internal lateinit var dt: TextView
        internal lateinit var bud: TextView
        internal lateinit var pdf: TextView
        internal lateinit var tar: TextView

        init {

            trpid = rowView.findViewById(R.id.trpid) as TextView
            nm = rowView.findViewById(R.id.nm) as TextView
            dt = rowView.findViewById(R.id.dt) as TextView
            pdf = rowView.findViewById(R.id.pdf) as TextView
            bud = rowView.findViewById(R.id.bud) as TextView
            tar = rowView.findViewById(R.id.tar) as TextView

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {

        }
    }
    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }
}