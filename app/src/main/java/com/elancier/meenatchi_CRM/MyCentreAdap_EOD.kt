package com.elancier.meenatchi_CRM


import android.app.LauncherActivity.ListItem
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elancier.meenatchi_CRM.DataClass.OrderDetail


class MyCustomerAdap_EOD(private val CentreArrays: List<OrderDetail>, private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{


    interface OnItemClickListener {
        fun OnItemClick(view:View,position:Int,viewType:Int)
    }

    private val listItems: List<ListItem>? =
        null
    private  var filterList:kotlin.collections.MutableList<ListItem?>? = null


    override fun onCreateViewHolder(viewGroup:ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.customer_list_item, viewGroup, false)
                return ItemViewHolder(productView)
            }
            else -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.customer_list_item, viewGroup, false)
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
                holder.pldate.setText(CentreArrays[position].mobile)
                holder.targetval.setText(CentreArrays[position].city+" , "+CentreArrays[position].state)
                holder.daysval.setText(CentreArrays[position].address)
                holder.budgetval.setText("Contact - "+CentreArrays[position].contact_person)

                if(CentreArrays[position].image1!!.isNotEmpty()) {
                    Glide.with(context).load(CentreArrays[position].image1).into(holder.imageView8)
                }
                else{
                    Glide.with(context).load(R.mipmap.noimage).into(holder.imageView8)

                }
                holder.frmdate.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel"+":"+CentreArrays[position].mobile));
                    context.startActivity(intent);


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

                (context as EOD_Customers_List).EditOffline(CentreArrays[position].id.toString(),CentreArrays[position].customer_name.toString(),position,CentreArrays[position])

            } catch (e: Exception) {

            }


        }

    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }
}