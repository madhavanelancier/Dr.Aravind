package com.elancier.meenatchi_CRM

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_report__dashboard.*

class Report_Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report__dashboard)

        var type=""
        c1.setOnClickListener {
            startActivity(Intent(this@Report_Dashboard,Family_report::class.java))
        }

        c2.setOnClickListener {
            type="plant"
            startActivity(Intent(this@Report_Dashboard,Plant_Harvest_report::class.java))
        }

        c3.setOnClickListener {

            startActivity(Intent(this@Report_Dashboard,Fish_report_list::class.java))
        }

    }
}
