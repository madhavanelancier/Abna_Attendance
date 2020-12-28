package com.elancier.abnaattendance

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_profile_purple.*

class General_Info : AppCompatActivity() {
    private val mTextMessage: TextView? = null
    private var navigation: BottomNavigationView? = null
    private val search_bar: View? = null
    var utils:Utils?=null
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_purple)

        utils=Utils(this)
        mob.setText(utils!!.getmobile())
        mail.setText(utils!!.getemail())
        pin.setText(utils!!.getfixed_salary())
        address.setText(utils!!.getaddress())
        city.setText(utils!!.getcity())
        state.setText(utils!!.getstate())
        name.setText(utils!!.getname())
        Glide.with(this).load(utils!!.getimage()).into(image)

        imageButton3.setOnClickListener {
            finish()
        }

        mob.setEnabled(false)
        mail.setEnabled(false)
        pin.setEnabled(false)
        address.setEnabled(false)
        city.setEnabled(false)
        state.setEnabled(false)

        edpro.setOnClickListener {
            mob.setEnabled(true)
            mail.setEnabled(true)
            pin.setEnabled(true)
            address.setEnabled(true)
            city.setEnabled(true)
            state.setEnabled(true)
            mob.requestFocus()

        }

    }
}