package com.elancier.meenatchi_CRM


import android.app.AlertDialog
import android.app.LauncherActivity.ListItem
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.nfc.cardemulation.CardEmulation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elancier.meenatchi_CRM.DataClass.OrderDetail
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MyCustomerAdap(private val CentreArrays: List<OrderDetail>, private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{


    interface OnItemClickListener {
        fun OnItemClick(view:View,position:Int,viewType:Int)
    }

    private val listItems: List<ListItem>? =
        null
    private  var filterList:kotlin.collections.MutableList<ListItem?>? = null

    internal lateinit var pref: SharedPreferences
    internal lateinit var editor: SharedPreferences.Editor

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
                pref = context.getSharedPreferences("MyPref", 0)
                editor = pref.edit()



                holder.docname.setText(CentreArrays[position].customer_name)
                holder.designation.setText(CentreArrays[position].mobile)
                holder.location.setText(CentreArrays[position].address+" , "+CentreArrays[position].city)
                Glide.with(context).load(CentreArrays[position].image).into(holder.imageView8)

                if(CentreArrays[position].status=="1"){
                    holder.excnval.visibility=View.VISIBLE
                }
                else{
                    holder.excnval.visibility=View.GONE
                }

                holder.viewDoctor.setOnClickListener {
                    context.startActivity(Intent(context,Customer_Add::class.java)
                        .putExtra("from","Edit")
                        .putExtra("id",CentreArrays[position].id))
                }

                holder.nav.setOnClickListener {
                    context.startActivity(Intent(context,Customer_Add::class.java)
                        .putExtra("from","Edit")
                        .putExtra("id",CentreArrays[position].id))
                }
                //holder.daysval.setText(CentreArrays[position].address)
                //holder.budgetval.setText("Employee - "+CentreArrays[position].contact_person)

               /* try {
                    if (CentreArrays[position].image1!!.isNotEmpty()) {
                        Glide.with(context).load(CentreArrays[position].image1)
                            .into(holder.imageView8)
                    } else {
                        Glide.with(context).load(R.mipmap.noimage).into(holder.imageView8)

                    }
                }
                catch (e:java.lang.Exception){
                    Glide.with(context).load(R.mipmap.noimage).into(holder.imageView8)

                }

                if(CentreArrays[position].city=="0"){
                    holder.frmdate.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                }
                else if(CentreArrays[position].city=="1"){
                    holder.frmdate.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))

                }
                else if(CentreArrays[position].city=="2"){


                }

                holder.frmdate.setOnClickListener {

                    if(CentreArrays[position].city=="0"){
                        val alert= AlertDialog.Builder(context)
                        alert.setTitle("Action Required")
                        alert.setMessage("Please collect payment or do any entries before checkout")
                        alert.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, i ->
                            dialog.dismiss()

                        })
                        alert.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, i ->
                            dialog.dismiss()


                        })
                        val pop=alert.create()
                        pop.show()
                    }
                    else if(CentreArrays[position].city=="1"){
                        (context as Customers_List).checkout("",CentreArrays[position].id.toString())
                    }
                    else if(CentreArrays[position].city=="2"){
                        //(context as Customers_List).checkout("",pref.getString("cusid","").toString())
                        (context as Customers_List).dosavecat(CentreArrays[position].customer_name.toString(),
                            CentreArrays[position].id.toString())
                    }

                    //(context as Customers_List).seconddashboard(CentreArrays[position].customer_name.toString(),
                    //CentreArrays[position].id.toString())



                }*/

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

        internal lateinit var docname: TextView
        internal lateinit var designation: TextView
        internal lateinit var location: TextView
        internal lateinit var imageView8: CircleImageView
        internal lateinit var viewDoctor: ImageButton
        internal lateinit var budgetval: TextView

        internal lateinit var frmdate: Button
        internal lateinit var todate: TextView
        internal lateinit var excnval: TextView
        internal lateinit var newcnval: TextView
        internal lateinit var tidval: TextView
        internal lateinit var nav: CardView




        init {

            docname=rowView.findViewById(R.id.textView87) as TextView
            docname=rowView.findViewById(R.id.textView87) as TextView
            designation=rowView.findViewById(R.id.textView88) as TextView
            location=rowView.findViewById(R.id.textView89) as TextView
            excnval=rowView.findViewById(R.id.textView86) as TextView
            nav=rowView.findViewById(R.id.nav) as CardView
            viewDoctor=rowView.findViewById(R.id.call) as ImageButton
            imageView8=rowView.findViewById(R.id.circleImageView) as CircleImageView
         /*   budgetval=rowView.findViewById(R.id.textView69) as TextView
            frmdate=rowView.findViewById(R.id.button8) as Button
            imageView8=rowView.findViewById(R.id.imageView8) as ImageView*/


            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            try {
                listener.OnItemClick(view, adapterPosition,
                    ITEM_CONTENT_VIEW_TYPE
                )

             /*   (context as Customers_List).dosavecat(CentreArrays[position].customer_name.toString(),
                    CentreArrays[position].id.toString())*/

            } catch (e: Exception) {

            }


        }

    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }
}