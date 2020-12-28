package com.elancier.abnaattendance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bank_details.*

class Bank_Details : AppCompatActivity() {
    var utils:Utils?=null
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_details)
        utils=Utils(this)
        pdate.setText(utils!!.getacc_name())
        ftime.setText(utils!!.getacc_no())
        todates.setText(utils!!.getbank_name())
        branch.setText(utils!!.getbranch_name())
        ifsc.setText(utils!!.getifsc_code())



    }
}