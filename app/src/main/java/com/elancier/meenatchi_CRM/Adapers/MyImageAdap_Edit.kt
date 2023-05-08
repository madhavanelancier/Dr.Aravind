package com.elancier.meenatchi_CRM.Adapers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


import java.text.SimpleDateFormat

import android.app.AlertDialog
import com.elancier.meenatchi_CRM.Family.Family_Main


class MyImageAdap_Edit(private val CentreArrays: List<String>, private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{


    interface OnItemClickListener {
        fun OnItemClick(view:View, position:Int, viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup:ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(com.elancier.meenatchi_CRM.R.layout.image_items, viewGroup, false)
                return ItemViewHolder(productView)
            }
            else -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(com.elancier.meenatchi_CRM.R.layout.image_items, viewGroup, false)
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

                //Glide.with(context).load(CentreArrays[position]).into(holder.image)

                //if(!CentreArrays[position].toString().contains("http")) {
                //    holder.image.setImageBitmap(CentreArrays[position])
                //}
                //else if(CentreArrays[position].toString().contains("http")) {
                    Glide.with(context).load(CentreArrays[position].trim()).into(holder.image)


                holder.edit.setOnClickListener {
                    val options = arrayOf<CharSequence>("Edit Photo", "Remove Photo", "Cancel")
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Select Action")
                    builder.setItems(options) { dialog, item ->
                        if (options[item] == "Edit Photo") {
                            (context as Family_Main).authenticationFragment!!.selectImageedit(position.toString())
                        } else if (options[item] == "Remove Photo") {

                            (context as Family_Main).authenticationFragment!!.removeImageedit(position.toString())

                        } else if (options[item] == "Cancel") {
                            dialog.dismiss()
                        }
                    }
                    builder.show()
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

        internal lateinit var image: ImageView
        internal lateinit var loanwk: Switch
        internal lateinit var loanamt: TextView
        internal lateinit var closed: TextView
        internal lateinit var edit: ImageButton

        internal lateinit var savings: TextView
        internal lateinit var od: TextView
        internal lateinit var pod: TextView



        init {

            image = rowView.findViewById(com.elancier.meenatchi_CRM.R.id.imageView3) as ImageView
            edit = rowView.findViewById(com.elancier.meenatchi_CRM.R.id.imageButton3) as ImageButton


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