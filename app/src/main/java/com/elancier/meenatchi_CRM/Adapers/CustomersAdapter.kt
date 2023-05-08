package com.elancier.meenatchi_CRM.Adapers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elancier.meenatchi_CRM.R
import com.elancier.meenatchi_CRM.retrofit.Customers

class CustomersAdapter(private val context: Context, private val items: ArrayList<Customers>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    interface OnItemClickListener {
        fun OnItemClick(view: View, position:Int, viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.customers_list, viewGroup, false)
        return ItemViewHolder(productView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder
                val cust_name = if(items[position].comment.isNullOrEmpty()) "" else items[position].comment
                val mobile = if(items[position].date.isNullOrEmpty()) "" else items[position].date
                holder.cust_name.setText(cust_name)
                holder.mobile.setText(mobile)
            }
            else -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_CONTENT_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemViewHolder internal constructor(rowView: View) : RecyclerView.ViewHolder(rowView), View.OnClickListener {

        internal var image: ImageView
        internal var cust_name: TextView
        internal var mobile: TextView
        internal var address: TextView


        init {

            image = rowView.findViewById(R.id.image) as ImageView
            cust_name = rowView.findViewById(R.id.cust_name) as TextView
            mobile = rowView.findViewById(R.id.mobile) as TextView
            address = rowView.findViewById(R.id.address) as TextView

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            try {

            } catch (e: Exception) {

            }


        }

        private var fRecords: Filter? = null


        /*void filter(String text){
        List<Reportssslistbo> temp = new ArrayList<Reportssslistbo>();
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
        /*private inner class RecordFilter : Filter() {
            protected override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()

                //Implement filter logic
                // if edittext is null return the actual list
                if (constraint == null || constraint.length == 0) {
                    //No need for filter
                    results.values = mRecyclerViewItems
                    results.count = mRecyclerViewItems.size()
                } else {
                    //Need Filter
                    // it matches the text  entered in the edittext and set the data in adapter list
                    val fRecords: ArrayList<Reportssslistbo> = ArrayList<Reportssslistbo>()
                    for (s in mRecyclerViewItems) {
                        val s2: Reportssslistbo = s as Reportssslistbo
                        val name: Boolean = s2.getPname().toUpperCase().trim()
                            .contains(constraint.toString().toUpperCase().trim { it <= ' ' })
                        val mob: Boolean = s2.getPhno().toUpperCase().trim()
                            .contains(constraint.toString().toUpperCase().trim { it <= ' ' })
                        val tour: Boolean = s2.getTour_name().toUpperCase().trim()
                            .contains(constraint.toString().toUpperCase().trim { it <= ' ' })
                        if (name || mob || tour) {
                            fRecords.add(s2)
                        }
                    }
                    results.values = fRecords
                    results.count = fRecords.size
                }
                return results
            }

            protected override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults
            ) {

                //it set the data from filter to adapter list and refresh the recyclerview adapter
                mRecyclerViewItems = results.values as ArrayList<Any?>
                notifyDataSetChanged()
            }
        }


    }*/
    }
    companion object {
        internal val ITEM_CONTENT_VIEW_TYPE = 1
    }




}