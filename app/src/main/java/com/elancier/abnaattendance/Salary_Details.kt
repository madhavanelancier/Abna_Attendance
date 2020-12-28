package com.elancier.abnaattendance

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.elancier.peace_admin.Appconstands
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_profile_dark.*

class Salary_Details : AppCompatActivity() {
    private val mTextMessage: TextView? = null
    private var navigation: BottomNavigationView? = null
    private val search_bar: View? = null
    var utils: Utils? = null
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_dark)
        utils=Utils(this)

        Glide.with(this).load(utils!!.getimage()).into(image)
        name.setText(utils!!.getname())
        fixedsalary.setText("Fixed Salary - "+Appconstands.rupees+utils!!.getfixed_salary())

        present.setText(Appconstands.rupees+utils!!.getcurrent_salary())
        absent.setText(Appconstands.rupees+utils!!.get_absent_salary())
        total_salary.setText(Appconstands.rupees+utils!!.getcurrent_salary())

        days.setText(utils!!.get_total_present()+" Day(s)")
        abdays.setText(utils!!.get_total_absent()+" Day(s)")
        permission_days.setText(utils!!.get_total_permission()+" Day(s)")
        late_days.setText(utils!!.get_total_late()+" Day(s)")

        back.setOnClickListener {
            finish()
        }

    }
}