package com.elancier.vanithamarket

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elancier.meenatchi_CRM.Appconstands
import com.elancier.meenatchi_CRM.DataClasses.CentresData
import com.elancier.meenatchi_CRM.R
import kotlinx.android.synthetic.main.activity_expense_add.*


import java.text.SimpleDateFormat

class SelectListAdap(private val CentreArrays: List<CentresData>,private val SpinnerIDs: List<String>,private val SpinnerNames: List<String>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{


    interface OnItemClickListener {
        fun OnItemClick(view:View,position:Int,viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup:ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.selectpro_list_item, viewGroup, false)
                return ItemViewHolder(productView)
            }
            else -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.selectpro_list_item, viewGroup, false)
                return ItemViewHolder(productView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder

                var sel=""
                holder.location.setText("Product: "+CentreArrays[position].CName)
                holder.appamt.setText("Brand: "+CentreArrays[position].prodpos)
                holder.stocks.setText("Available Stocks: "+CentreArrays[position].CMembers)

                var adap=ArrayAdapter(context,R.layout.support_simple_spinner_dropdown_item,SpinnerNames)
                holder.pldate.adapter=adap
                if(CentreArrays[position].CLocation=="0")
                {
                    holder.targetval.setText("")

                }
                else{
                    holder.targetval.setText(CentreArrays[position].CLocation)

                }


                holder.remove.setOnClickListener {
                    (context as Prolist).removeitem(position,CentreArrays[position].time1.toString(),CentreArrays[position].time2.toString())
                }

                if(CentreArrays[position].time1!="0"){
                    for(i in 0 until SpinnerIDs.size){
                        if(SpinnerIDs[i]==CentreArrays[position].time1){
                            holder.pldate.setSelection(i)
                            sel="0"
                        }
                    }
                }

                holder.pldate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, positions: Int, id: Long) {
                      if(positions!=0){

                        if(sel.isEmpty()) {
                            (context as Prolist).disc(
                                position,
                                SpinnerIDs[holder.pldate.selectedItemPosition].toString(),
                                CentreArrays[position].time2.toString()
                            )
                        }



                      }
                        else{
                                if(CentreArrays[position].time1!="0") {
                                    (context as Prolist).discremove(
                                        position,
                                        "0",
                                        CentreArrays[position].time2.toString()
                                    )
                                }
                      }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }

                holder.targetval.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if(s!!.isNotEmpty()){
                            if(s.toString().toInt()<=CentreArrays[position].CMembers!!.toInt()){
                                (context as Prolist).total(position,holder.targetval.text.toString().toString())
                            }
                            else{
                                holder.targetval.setText("")
                                Toast.makeText(context,CentreArrays[position].CMembers+" stocks only available",Toast.LENGTH_LONG).show()
                            }
                        }

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    }

                })

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

    inner class ItemViewHolder internal constructor(rowView: View) : RecyclerView.ViewHolder(rowView) {

        internal lateinit var location: TextView
        internal lateinit var pldate: Spinner
        internal lateinit var targetval: EditText
        internal lateinit var imageView8: ImageView
        internal lateinit var daysval: TextView
        internal lateinit var budgetval: TextView

        internal lateinit var frmdate: Button
        internal lateinit var status: TextView
        internal lateinit var appamt: TextView
        internal lateinit var stocks: TextView
        internal lateinit var tidval: TextView
        internal lateinit var remove: ImageButton
        internal lateinit var cards: CardView




        init {

            location=rowView.findViewById(R.id.textView27) as TextView
            pldate=rowView.findViewById(R.id.disc) as Spinner
            targetval=rowView.findViewById(R.id.qty) as EditText
            appamt=rowView.findViewById(R.id.textView27brand) as TextView
            stocks=rowView.findViewById(R.id.stock) as TextView
            remove=rowView.findViewById(R.id.imageButton9) as ImageButton

        }


    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }
}