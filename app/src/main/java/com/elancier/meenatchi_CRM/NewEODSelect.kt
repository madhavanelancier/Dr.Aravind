package com.elancier.meenatchi_CRM

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class NewEODSelect : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_eod_select)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowHomeEnabled(true)
        ab.title = "EOD"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    fun openEOD(view: View){
        startActivity(
            Intent(this, Eod_list::class.java).putExtra("cus", "customer")//Eod_list
        )
    }

    fun openFRONT(view: View){
        startActivity(
            Intent(this, Frontsheet::class.java).putExtra("cus", "customer")//Eod_list
        )
    }

}