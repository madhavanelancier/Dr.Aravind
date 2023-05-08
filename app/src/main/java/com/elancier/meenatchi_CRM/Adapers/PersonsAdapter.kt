package com.elancier.domdox.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.elancier.meenatchi_CRM.R
import org.json.JSONArray

/*class PersonsAdapter(
        private val context: Activity, //to store the list of countries
        private val Persons: ArrayList<ContactPerson>) : ArrayAdapter<Any>(context, R.layout.family_list_adapter,
    (Persons as List<Any>?)!!
) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        //val inflater = context.layoutInflater
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.person_list_adapter, null, true)
        val nm = rowView.findViewById<TextView>(R.id.nm)
        val mob = rowView.findViewById<TextView>(R.id.mob)
        val desig = rowView.findViewById<TextView>(R.id.desig)

        nm.setText(Persons[position].Name)
        mob.setText(Persons[position].mob)
        desig.setText(Persons[position].desig)

        return rowView

    }
}*/

class PersonsAdapter(private val context: Context, private val Persons: JSONArray, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{


    interface OnItemClickListener {
        fun OnItemClick(view:View, position:Int, viewType:Int)
    }


    override fun onCreateViewHolder(viewGroup:ViewGroup, viewType:Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.person_list_adapter, viewGroup, false)
                return ItemViewHolder(productView)
            }
            else -> {
                val productView = LayoutInflater.from(viewGroup.context).inflate(R.layout.person_list_adapter, viewGroup, false)
                return ItemViewHolder(productView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            ITEM_CONTENT_VIEW_TYPE -> {
                val itemViewHolder = holder as ItemViewHolder

                holder.nm.setText(Persons.getJSONObject(position).getString("name"))
                holder.mob.setText(Persons.getJSONObject(position).getString("mob"))
                holder.desig.setText(Persons.getJSONObject(position).getString("desig"))

            }
            else -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_CONTENT_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return Persons.length()
    }

    inner class ItemViewHolder internal constructor(rowView: View) : RecyclerView.ViewHolder(rowView), View.OnClickListener {

        internal lateinit var nm: TextView
        internal lateinit var mob: TextView
        internal lateinit var desig: TextView



        init {
            nm = rowView.findViewById<TextView>(R.id.nm) as TextView
            mob = rowView.findViewById<TextView>(R.id.mob) as TextView
            desig = rowView.findViewById<TextView>(R.id.desig) as TextView

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