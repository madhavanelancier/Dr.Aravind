package com.elancier.meenatchi_CRM.Adapers

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.elancier.meenatchi_CRM.DataClasses.CentresData
import com.elancier.meenatchi_CRM.Family.Harvest_Family
import com.elancier.meenatchi_CRM.Harvest.Harvest_Main
import com.elancier.meenatchi_CRM.MainActivity
import com.elancier.meenatchi_CRM.R


import java.text.SimpleDateFormat

class MyHarvestAdap(private val CentreArrays: List<CentresData>, private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{


    interface OnItemClickListener {
        fun OnItemClick(view:View, position:Int, viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup:ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.harvest_items, viewGroup, false)
                return ItemViewHolder(productView)
            }
            else -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.harvest_items, viewGroup, false)
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
                holder.loandt.isSelected=true
                holder.loandt.setText(CentreArrays[position].CName)

                //holder.loanamt.setText(CentreArrays[position].city+","+CentreArrays[position].state)
                holder.closed.setText(CentreArrays[position].CMembers)
                val k=CentreArrays[position].CStatus.toString()
                if(k=="on"){
                    holder.loanwk.isChecked=true
                    holder.loandt.setTextColor(Color.parseColor("#555555"))

                    holder.loanamt.setTextColor(Color.parseColor("#000000"))

                    holder.closed.setTextColor(Color.parseColor("#008577"))

                    holder.harvest.background=context.resources.getDrawable(R.drawable.rectangle_green)
                    //holder.edit.visibility=View.VISIBLE
                    holder.harvest.isEnabled=true
                    holder.harvest.setTextColor(Color.parseColor("#FFFFFF"))

                }
                else{
                    holder.loanwk.isChecked=false
                    holder.loandt.setTextColor(Color.parseColor("#BEBFC1"))
                    holder.loanamt.setTextColor(Color.parseColor("#BEBFC1"))
                    holder.closed.setTextColor(Color.parseColor("#BEBFC1"))
                    //holder.edit.visibility=View.GONE
                    holder.harvest.setTextColor(Color.parseColor("#cccccc"))
                    holder.harvest.background=context.resources.getDrawable(R.drawable.rectangle_black)
                    holder.harvest.isEnabled=false
                }

                holder.harvest.setOnClickListener {
                    context.startActivity(Intent(context,Harvest_Main::class.java).putExtra("id",CentreArrays[position].Cid.toString()).putExtra("vid",(context as Harvest_Family).posid))

                }


                holder.loanwk.setOnClickListener {
                    Log.e("jkk","lkl")

                    if(k=="on"){
                       // (context as Branch_List).dosavedel(CentreArrays[position].Cid.toString())

                    }
                    else{
                       // (context as Branch_List).dosavedelenable(CentreArrays[position].Cid.toString())

                    }

                }

                holder.edit.setOnClickListener {
                    Log.e("jkk","lkl")

                   (context as MainActivity).editarea(CentreArrays[position].Cid.toString())
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

        internal lateinit var loandt: TextView
        internal lateinit var loanwk: Switch
        internal lateinit var loanamt: TextView
        internal lateinit var closed: TextView
        internal lateinit var edit: ImageButton

        internal var sts: TextView
        internal lateinit var harvest: Button

        internal lateinit var savings: TextView
        internal lateinit var od: TextView
        internal lateinit var pod: TextView



        init {

            loandt = rowView.findViewById(R.id.textView15) as TextView
            loanwk = rowView.findViewById(R.id.textView16) as Switch
            loanamt = rowView.findViewById(R.id.textView17) as TextView
            closed = rowView.findViewById(R.id.textView22) as TextView
            sts = rowView.findViewById(R.id.textView18) as TextView
            harvest = rowView.findViewById(R.id.button7) as Button
            savings= rowView.findViewById(R.id.imageView5) as TextView
            od=rowView.findViewById(R.id.textView20) as TextView
            edit=rowView.findViewById(R.id.imageButton) as ImageButton
            pod=rowView.findViewById(R.id.textView21) as TextView

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