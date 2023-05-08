package com.elancier.meenatchi_CRM.Adapers

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elancier.meenatchi_CRM.*
import org.json.JSONArray

class ExpenseListAdapter(private var items: JSONArray, private val context: Activity, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable
{

    interface OnItemClickListener {
        fun OnRequestItemClick(view: View, position:Int, viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.exp_list_item, viewGroup, false)
                return ItemViewHolder(productView)
            }
            else -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.exp_list_item, viewGroup, false)
                return ItemViewHolder(productView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder
                val obj = items.getJSONObject(position)
                val id = if (obj.has("id")) obj.getString("id") else ""
                val type = if (obj.has("type")) if(obj.getString("type")!="null")obj.getString("type") else "" else ""
                val name = if (obj.has("name")) if(obj.getString("name")!="null")obj.getString("name") else "" else ""
                val user = if (obj.has("user")) if(obj.getString("user")!="null")obj.getString("user") else "" else ""
                val tid = if (obj.has("tid")) if(obj.getString("tid")!="null")obj.getString("tid") else "" else ""
                val image = if (obj.has("image")) if(obj.getString("image")!="null")obj.getString("image") else "" else ""
                val image1 = if (obj.has("image1")) if(obj.getString("image1")!="null")obj.getString("image1") else "" else ""
                val hotel_name = if (obj.has("hotel_name")) if(obj.getString("hotel_name")!="null")obj.getString("hotel_name") else "" else ""
                val city = if (obj.has("city")) if(obj.getString("city")!="null")obj.getString("city") else "" else ""
                val from = if (obj.has("from")) if(obj.getString("from")!="null")obj.getString("from") else "" else ""
                val to = if (obj.has("to")) if(obj.getString("to")!="null")obj.getString("to") else "" else ""
                val by = if (obj.has("by")) if(obj.getString("by")!="null")obj.getString("by") else "" else ""
                val description = if (obj.has("description")) if(obj.getString("description")!="null")obj.getString("description") else "" else ""
                val date = if (obj.has("date")) if(obj.getString("date")!="null")obj.getString("date") else "" else ""
                val amount = if (obj.has("amount")) Appconstands.rupees+obj.get("amount") else ""

                holder.date.setText(date)
                if (name.isNotEmpty()) {
                    holder.location.setText(name)
                }else if (hotel_name.isNotEmpty()){
                    holder.location.setText(hotel_name)
                }else if (from.isNotEmpty()){
                    holder.location.setText(""+from+" to "+to)
                }
                holder.pldate.setText(amount)
                holder.e_type.setText(type)
                if (by.isNotEmpty()){
                    holder.desctit.visibility=View.VISIBLE
                    holder.note.visibility=View.VISIBLE
                    holder.desctit.setText("by : ")
                    holder.note.setText(by)
                }else if (description.isNotEmpty()) {
                    holder.desctit.visibility=View.VISIBLE
                    holder.note.visibility=View.VISIBLE
                    holder.note.setText("Description : ")
                    holder.note.setText(description)
                }else if (city.isNotEmpty()){
                    holder.desctit.visibility=View.VISIBLE
                    holder.note.visibility=View.VISIBLE
                    holder.note.setText("City : ")
                    holder.note.setText(city)
                }
                if(image!!.isNotEmpty()) {
                    Glide.with(context).load(image).error(R.mipmap.noimage).into(holder.imageView8)
                }
                else{
                    Glide.with(context).load(R.mipmap.noimage).into(holder.imageView8)

                }

                holder.delete.setOnClickListener {

                    var AlertDialog= AlertDialog.Builder(context)
                    AlertDialog.setTitle("Delete?")
                    AlertDialog.setMessage("Are you sure want to delete this entry?")
                    AlertDialog.setPositiveButton("Yes",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            (context as Request_List).delete(id)
                            dialogInterface.dismiss()


                        })
                    AlertDialog.setNegativeButton("No",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()

                        })
                    val pops=AlertDialog.create()
                    pops.show()
                }
                /*holder.cards.setOnClickListener {
                    (context as Request_List).imgpo(position,"Request")
                }*/
            }
            else -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_CONTENT_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return items.length()
    }

    inner class ItemViewHolder internal constructor(rowView: View) : RecyclerView.ViewHolder(rowView), View.OnClickListener {

        internal var location: TextView
        internal var pldate: TextView
        internal var e_type: TextView
        internal var imageView8: ImageView
        internal var date: TextView
        internal var note: TextView
        internal var budgetval: TextView
        internal var delete: ImageButton
        internal var desctit: TextView
        internal var cards: CardView
        internal var frmdate: Button

        init {

            location=rowView.findViewById(R.id.textView65) as TextView
            pldate=rowView.findViewById(R.id.textView66) as TextView
            e_type=rowView.findViewById(R.id.textView67) as TextView
            note=rowView.findViewById(R.id.textView68) as TextView
            date=rowView.findViewById(R.id.textView28) as TextView
            delete=rowView.findViewById(R.id.imageButton8) as ImageButton
            budgetval=rowView.findViewById(R.id.textView69) as TextView
            desctit=rowView.findViewById(R.id.textView71) as TextView
            cards=rowView.findViewById(R.id.cards) as CardView
            frmdate=rowView.findViewById(R.id.button8) as Button
            imageView8=rowView.findViewById(R.id.imageView8) as ImageView
            itemView.setOnClickListener(this)


        }
        override fun onClick(view: View) {
            /*try {
                listener.OnRequestItemClick(view, adapterPosition,
                        ITEM_CONTENT_VIEW_TYPE
                )
                //(context as Customers_List).Editorders(position)
            } catch (e: Exception) {

            }*/
            /*val obj = items.getJSONObject(position)
            val statusval = if (obj.has("status")) obj.getString("status") else ""
            if (statusval.toString().toLowerCase() == "pending") {
                val yu = Intent(context, AdvanceRequest::class.java)
                yu.putExtra("data", items.getJSONObject(adapterPosition).toString())
                context.startActivity(yu)
            }*/
        }

    }

    /*fun filter(text:String){
        val temp = JSONArray();
        for(Object d: mRecyclerViewItems){
            Reportssslistbo item = (Reportssslistbo) d;
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(item.getTour_name().contains(text)){
                temp.add(item);
            }
        }
        //update recyclerview
        disp_adapter.updateList(temp);
    }*/
    //filter class
    private inner class RecordFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()

            //Implement filter logic
            // if edittext is null return the actual list
            if (constraint == null || constraint.length == 0) {
                //No need for filter
                results.values = items
                results.count = items.length()
            } else {
                //Need Filter
                // it matches the text  entered in the edittext and set the data in adapter list
                val fRecords: JSONArray = JSONArray()
                for(s in 0 until items.length()){
                    val s2 = items.getJSONObject(s)//s
                    val name: Boolean = s2.getString("name").toUpperCase().trim()
                            .contains(constraint.toString().toUpperCase().trim { it <= ' ' })
                    val hotel_name: Boolean = s2.getString("hotel_name").toUpperCase().trim()
                            .contains(constraint.toString().toUpperCase().trim { it <= ' ' })
                    val from: Boolean = s2.getString("from").toUpperCase().trim()
                            .contains(constraint.toString().toUpperCase().trim { it <= ' ' })
                    val to: Boolean = s2.getString("to").toUpperCase().trim()
                            .contains(constraint.toString().toUpperCase().trim { it <= ' ' })
                    if (name || hotel_name || from|| to) {
                        fRecords.put(s2)
                    }
                }
                results.values = fRecords
                results.count = fRecords.length()
            }
            return results
        }

        override fun publishResults(
            constraint: CharSequence?,
            results: FilterResults
        ) {

            //it set the data from filter to adapter list and refresh the recyclerview adapter
            items = results.values as JSONArray
            notifyDataSetChanged()
        }
    }

    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }

    override fun getFilter(): Filter {
        return RecordFilter()
    }
}