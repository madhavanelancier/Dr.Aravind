package com.elancier.meenatchi_CRM.Adapers


import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elancier.meenatchi_CRM.Camp_list
import com.elancier.meenatchi_CRM.Eod_list
import com.elancier.meenatchi_CRM.Meeting_Add
import com.elancier.meenatchi_CRM.Camp_leads
import com.elancier.meenatchi_CRM.R
import com.elancier.meenatchi_CRM.Schedule_Meeting

class PreviousCampAdap(private val context: Context, private val CentreArrays: ArrayList<Camp_leads.EOD_data>, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    interface OnItemClickListener {
        fun OnItemClick(view:View,position:Int,viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup:ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.prev_camp_list_item, viewGroup, false)
        return ItemViewHolder(productView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder

                holder.camp.setText(CentreArrays[position].cname)
                holder.crmattr.setText(CentreArrays[position].mmob)
                holder.wifenm.setText(CentreArrays[position].maddress)
                holder.husnm.setText(CentreArrays[position].city)
                holder.wifenum.setText(CentreArrays[position].email)
                holder.husnum.setText(CentreArrays[position].state)
                holder.prefloc.setText(CentreArrays[position].tval)

                holder.edit.setOnClickListener {
                    context.startActivity(Intent(context,Schedule_Meeting::class.java)
                        .putExtra("from","Edit")
                        .putExtra("id",CentreArrays[position].customerID.toString())
                        .putExtra("dcname",CentreArrays[position].cname.toString())
                        .putExtra("date",CentreArrays[position].mmob.toString())
                        .putExtra("status",CentreArrays[position].sval.toString()))
                }

               /* holder.feedback.setOnClickListener {
                    context.startActivity(Intent(context,Meeting_Add::class.java)
                        .putExtra("id",CentreArrays[position].customerID.toString())
                        .putExtra("dcname",CentreArrays[position].cname.toString())
                        .putExtra("date",CentreArrays[position].mmob.toString())
                        .putExtra("status",CentreArrays[position].sval.toString()))
                }*/
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
        return CentreArrays.size
    }

    inner class ItemViewHolder internal constructor(rowView: View) : RecyclerView.ViewHolder(rowView), View.OnClickListener {

        internal lateinit var camp: TextView
        internal lateinit var crmattr: TextView
        internal lateinit var wifenm: TextView
        internal lateinit var husnm: TextView
        internal lateinit var wifenum: TextView
        internal lateinit var husnum: TextView
        internal lateinit var prefloc: TextView

        internal lateinit var edit: Button



        init {

            camp = rowView.findViewById(R.id.camploc) as TextView
            crmattr = rowView.findViewById(R.id.crmattribute) as TextView
            wifenm = rowView.findViewById(R.id.wifenm) as TextView
            edit = rowView.findViewById(R.id.edit) as Button
            husnm = rowView.findViewById(R.id.husnm) as TextView
            wifenum = rowView.findViewById(R.id.wifenum) as TextView
            husnum = rowView.findViewById(R.id.husnum) as TextView
            prefloc = rowView.findViewById(R.id.prefloc) as TextView


            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            try {
                listener.OnItemClick(
                    view, adapterPosition,
                    ITEM_CONTENT_VIEW_TYPE
                )

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