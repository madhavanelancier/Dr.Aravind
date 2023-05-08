package com.elancier.meenatchi_CRM.retrofit

import android.util.Log
import android.view.View
import android.widget.GridView
import android.widget.ListView

object Helper {

    fun getListViewSize(myListView: ListView) {
        val myListAdapter = myListView.adapter
                ?: //do nothing return null
                return
//set listAdapter in loop for getting final size
        var totalHeight = 0
        for (size in 0 until myListAdapter.count) {
            val listItem = myListAdapter.getView(size, null, myListView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        //setting listview item in adapter
        val params = myListView.layoutParams
        params.height = totalHeight + myListView.dividerHeight * if (myListAdapter.count > 1) myListAdapter.count - 1 else myListAdapter.count
        myListView.layoutParams = params
        // print height of adapter on log
        Log.i("height of listItem:", totalHeight.toString())
    }

    fun getgridViewSize(listView: GridView) {

        val mAdapter = listView.adapter

        var totalHeight = 0

        for (i in 0 until mAdapter.count) {
            val mView = mAdapter.getView(i, null, listView)

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            //heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE == 2, MeasureSpec.AT_MOST);
            if ((i % 2) == 0) {
                // number is even
                //totalHeight += 245//mView.measuredHeight/2
                totalHeight += mView.measuredHeight/3//(i or  1)
            }

            else {
                // number is odd
            }
            Log.i("HEIGHT$i", totalHeight.toString())

        }

        val params = listView.layoutParams
        params.height = (totalHeight )//+ (mAdapter.count - 1))
        Log.i("heightttttttttttt",
                (totalHeight).toString() + "")
        listView.layoutParams = params
        listView.requestLayout()

    }
    fun setDynamicListHeight(listView: ListView) {
        val adapter = listView.adapter ?: return
        //check adapter if null
        var height = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.UNSPECIFIED)
        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            height += listItem.measuredHeight
        }
        val layoutParams = listView.layoutParams
        layoutParams.height = height + listView.dividerHeight * (adapter.count - 1)
        listView.layoutParams = layoutParams
        listView.requestLayout()
    }

    fun setDynamicHeight(listView: ListView) {
        val adapter = listView.adapter ?: return
        //check adapter if null
        var height = 0
        val desiredWidth =
            View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.UNSPECIFIED)
        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            height += listItem.measuredHeight
        }
        val layoutParams = listView.layoutParams
        layoutParams.height = height + listView.dividerHeight * (adapter.count - 1)
        println("height : "+height + listView.dividerHeight * (adapter.count - 1))
        listView.layoutParams = layoutParams
        listView.requestLayout()
    }

}