package com.elancier.meenatchi_CRM

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elancier.meenatchi_CRM.DataClass.OrderDetail

class MyOrderAdap(private val CentreArrays: List<OrderDetail>, private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{


    interface OnItemClickListener {
        fun OnItemClick(view:View,position:Int,viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup:ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.order_list_item, viewGroup, false)
                return ItemViewHolder(productView)
            }
            else -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.order_list_item, viewGroup, false)
                return ItemViewHolder(productView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder

                holder.location.setText(CentreArrays[position].customer_name)
                holder.pldate.setText(Appconstands.rupees+CentreArrays[position].mobile)
                holder.targetval.setText(CentreArrays[position].city)
                holder.daysval.setText(CentreArrays[position].address)
                if(CentreArrays[position].image!!.isNotEmpty()&&CentreArrays[position].image!! !="null") {
                    Glide.with(context).load(CentreArrays[position].image).into(holder.imageView8)
                }
                else{
                    Glide.with(context).load(R.mipmap.noimage).into(holder.imageView8)

                }

            }
            else -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        val recyclerViewItem = CentreArrays[position]
        return ITEM_CONTENT_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return CentreArrays.size
    }

    inner class ItemViewHolder internal constructor(rowView: View) : RecyclerView.ViewHolder(rowView), View.OnClickListener {

        internal lateinit var location: TextView
        internal lateinit var pldate: TextView
        internal lateinit var targetval: TextView
        internal lateinit var imageView8: ImageView
        internal lateinit var daysval: TextView
        internal lateinit var budgetval: TextView

        internal lateinit var frmdate: Button
        internal lateinit var todate: TextView
        internal lateinit var excnval: TextView
        internal lateinit var newcnval: TextView
        internal lateinit var tidval: TextView




        init {

            location=rowView.findViewById(R.id.textView65) as TextView
            pldate=rowView.findViewById(R.id.textView66) as TextView
            targetval=rowView.findViewById(R.id.textView67) as TextView
            daysval=rowView.findViewById(R.id.textView68) as TextView
            budgetval=rowView.findViewById(R.id.textView69) as TextView
            frmdate=rowView.findViewById(R.id.button8) as Button
            imageView8=rowView.findViewById(R.id.imageView8) as ImageView
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            try {
                listener.OnItemClick(view, adapterPosition,
                    ITEM_CONTENT_VIEW_TYPE
                )

                (context as Customers_List).Editorders(position)

            } catch (e: Exception) {

            }


        }

    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }
}