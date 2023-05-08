package com.elancier.meenatchi_CRM

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.elancier.meenatchi_CRM.DataClasses.CentresData


import java.text.SimpleDateFormat

class MySearchAdap(private val CentreArrays: List<CentresData>, private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{


    interface OnItemClickListener {
        fun OnItemClick(view: View, position: Int, viewType: Int)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.profile_items, viewGroup, false)
                return ItemViewHolder(productView)
            }
            else -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.profile_items, viewGroup, false)
                return ItemViewHolder(productView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder
                val formater = SimpleDateFormat("h:mm a")
                holder.name.isSelected=true
                holder.name.setText(CentreArrays[position].CName)
                holder.proids.setText(CentreArrays[position].Cid)
                holder.time.setText(CentreArrays[position].time2)
                holder.staff.setText(CentreArrays[position].CMembers)
                holder.day.setText(CentreArrays[position].time1)
                holder.closed.setText(CentreArrays[position].CStatus)
                holder.savings.setText(CentreArrays[position].CLocation)
                Log.e("insidedapa", CentreArrays[position].CName!!)

                if((context as Search_Activity).logid.isNotEmpty()){
                    holder.fulldetails.visibility=View.VISIBLE
                }
                else{
                    holder.fulldetails.visibility=View.GONE

                }

                if((context as Search_Activity).logid.isNotEmpty()&&(context as Search_Activity).balance.toInt()>0) {

                }

                holder.fulldetails.setOnClickListener {

                    val j=Intent(context,View_profile::class.java)
                    j.putExtra("idval",CentreArrays[position].Cid)
                    context.startActivity(j)
                }




            }
            else -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        val recyclerViewItem = CentreArrays[position]
        /*if (recyclerViewItem instanceof Confirmmodel) {
            return ITEM_CONFIRM_VIEW_TYPE;
        }*/
        return ITEM_CONTENT_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return CentreArrays.size
    }

    inner class ItemViewHolder internal constructor(rowView: View) : RecyclerView.ViewHolder(rowView), View.OnClickListener {

        internal lateinit var img: ImageView
        internal lateinit var name: TextView
        internal lateinit var proids: TextView
        internal lateinit var closed: TextView
        internal lateinit var edit: ImageButton
        internal lateinit var time:TextView
        internal lateinit var staff:TextView
        internal lateinit var day:TextView
        internal lateinit var savings: TextView
        internal lateinit var od: TextView
        internal lateinit var fulldetails: Button

        internal lateinit var pod: TextView



        init {

            img = rowView.findViewById(R.id.imageView12) as ImageView
            name = rowView.findViewById(R.id.textView20) as TextView
            proids = rowView.findViewById(R.id.textView21) as TextView
            time= rowView.findViewById(R.id.textView22) as TextView
            staff= rowView.findViewById(R.id.textView23) as TextView
            day= rowView.findViewById(R.id.textView26) as TextView
            closed= rowView.findViewById(R.id.textView24) as TextView
            savings= rowView.findViewById(R.id.textView25) as TextView
            fulldetails= rowView.findViewById(R.id.cardView8) as Button
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            try {
                listener.OnItemClick(view, adapterPosition,
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