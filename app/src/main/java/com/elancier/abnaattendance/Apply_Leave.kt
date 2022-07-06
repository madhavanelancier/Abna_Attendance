package com.elancier.abnaattendance

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SyncRequest
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elancier.peace_admin.Appconstands
import kotlinx.android.synthetic.main.activity_dashboard_apply_leave.*
import kotlinx.android.synthetic.main.activity_dashboard_permission.*
import kotlinx.android.synthetic.main.activity_dashboard_permission.ftime
import kotlinx.android.synthetic.main.activity_dashboard_permission.imageButton
import kotlinx.android.synthetic.main.activity_dashboard_permission.pdate
import kotlinx.android.synthetic.main.activity_dashboard_permission.reason
import kotlinx.android.synthetic.main.activity_dashboard_permission.signin
import kotlinx.android.synthetic.main.activity_dashboard_permission.textView14
import kotlinx.android.synthetic.main.activity_dashboard_permission.todates
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Apply_Leave : AppCompatActivity() {
    private var mYear = 0
    private  var mMonth:Int = 0
    private  var mDay:Int = 0
    private var CalendarHour = 0
    private  var CalendarMinute:Int = 0
    var format: String? = null
    var calendar: Calendar? = null
    var timepickerdialog: RangeTimePickerDialog? = null
    var activity=this
    var utils:Utils?=null
    var hrs=""
    var minutes=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_apply_leave)
        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        utils=Utils(this)

        var arr=ArrayList<String>()
        arr.add("Select DD")
        arr.add("Full Leave")
        arr.add("Half Leave")
        arr.add("Quarter Leave")
        val customAdapter = CustomeSpinner(activity, arr as java.util.ArrayList<String>)
        ddselect.adapter = customAdapter

        pdate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                    this,
                    { view, year, monthOfYear, dayOfMonth -> pdate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) },
                    mYear,
                    mMonth,
                    mDay
            )
            datePickerDialog.datePicker.minDate=System.currentTimeMillis()
            datePickerDialog.show()
        }

        imageButton.setOnClickListener {
            finish()
        }

        signin.setOnClickListener {
            if(pdate.text.toString().isNotEmpty()&&ftime.text.toString().isNotEmpty()
                    &&todates.text.toString().isNotEmpty()){
                UpdateInfoTask().execute(utils!!.getid(), pdate.text.toString(),
                        ftime.text.toString(), todates.text.toString(), hrs + "," + minutes, reason.text.toString())
            }
            else{
                if(pdate.text.toString().isEmpty()){
                    pdate.setError("Required field")
                }
                if(ftime.text.toString().isEmpty()){
                    ftime.setError("Required field")
                }
                if(todates.text.toString().isEmpty()){
                    todates.setError("Required field")
                }
            }
        }

        ftime.setOnClickListener {

            if(pdate.text.toString().isNotEmpty()) {
                calendar = Calendar.getInstance();
                CalendarHour = calendar!!.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar!!.get(Calendar.MINUTE);
                timepickerdialog = RangeTimePickerDialog(this@Apply_Leave,
                    { v, hourOfDay, minute ->
                        var hourOfDay = hourOfDay
                        if (hourOfDay == 0) {
                            hourOfDay += 12
                            format = " AM"
                        } else if (hourOfDay == 12) {
                            format = " PM"
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12
                            format = " PM"
                        } else {
                            format = " AM"
                        }
                        if (hourOfDay <= 9 || minute <= 9) {
                            if (hourOfDay <= 9 && minute <= 9) {
                                ftime.setText("0$hourOfDay:0$minute$format")
                            } else if (hourOfDay <= 9 && minute > 9) {
                                ftime.setText("0$hourOfDay:$minute$format")
                            } else if (hourOfDay > 9 && minute <= 9) {
                                ftime.setText("$hourOfDay:0$minute$format")
                            }

                        } else {
                            ftime.setText("$hourOfDay:$minute$format")

                        }
                    }, CalendarHour, CalendarMinute, false
                )

                val cal=Calendar.getInstance()
                val format=SimpleDateFormat("dd-MM-yyyy")
                val dates=format.format(cal.time)

                if(pdate.text.toString()==dates) {
                    timepickerdialog!!.setMin(CalendarHour, CalendarMinute)
                }

                timepickerdialog!!.show()
            }
            else{
                toast("Please select date")
            }
        }


        todates.setOnClickListener {
            if(pdate.text.toString().isNotEmpty()) {
                calendar = Calendar.getInstance();
                CalendarHour = calendar!!.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar!!.get(Calendar.MINUTE);
                timepickerdialog = RangeTimePickerDialog(this@Apply_Leave,
                    { view, hourOfDay, minute ->
                        var hourOfDay = hourOfDay
                        if (hourOfDay == 0) {
                            hourOfDay += 12
                            format = " AM"
                        } else if (hourOfDay == 12) {
                            format = " PM"
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12
                            format = " PM"
                        } else {
                            format = " AM"
                        }
                        if (hourOfDay <= 9 || minute <= 9) {
                            if (hourOfDay <= 9 && minute <= 9) {
                                todates.setText("0$hourOfDay:0$minute$format")
                            } else if (hourOfDay <= 9 && minute > 9) {
                                todates.setText("0$hourOfDay:$minute$format")
                            } else if (hourOfDay > 9 && minute <= 9) {
                                todates.setText("$hourOfDay:0$minute$format")
                            }
                        } else {
                            todates.setText("$hourOfDay:$minute$format")
                        }

                        calculate()
                    }, CalendarHour, CalendarMinute, false
                )
                timepickerdialog!!.show()
            }
            else{
                toast("Please select date")
            }
        }
        reason.setFocusable(true)
        reason.requestFocus()
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(reason, InputMethodManager.SHOW_IMPLICIT)



    }
    fun calculate(){
        val simpleDateFormat = SimpleDateFormat("hh:mm a")

        val date1 = simpleDateFormat.parse(ftime.text.toString())
        val date2 = simpleDateFormat.parse(todates.text.toString())

        val difference: Long = date2.getTime() - date1.getTime()
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        var hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60))
        val min = (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)
        hours = if (hours < 0) -hours else hours
        Log.i("======= Hours", " :: $hours")
        Log.i("======= min", " :: $min")

        hrs=hours.toString()
        minutes=min.toString()

        if(hours.toInt()==0&&min.toInt()>0){
            textView14.setText("Total Timing : " + hours + " hour " + min + " mins")
        }
        else if(hours.toInt()>0&&min.toInt()>0){
            textView14.setText("Total Timing : " + hours + " hour " + min + " mins")
        }
        else if(hours.toInt()>0&&min.toInt()==0){
            textView14.setText("Total Timing : " + hours + " hour")
        }
    }

    inner class UpdateInfoTask :
            AsyncTask<String?, Void?, String?>() {
        internal lateinit var pDialo : ProgressDialog
        //var statusval=""

        override fun onPreExecute() {
            // array.clear()
            pDialo = ProgressDialog(activity);
            pDialo.setMessage("Please wait...");
            pDialo.setIndeterminate(false);
            pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialo.setCancelable(false);
            //pDialo.setMax(3)
            pDialo.show()
        }

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: String?): String? {
            var result: String? = null
            val con =
                    Connection()
            //statusval= status.toString()
            try {

                val json= JSONObject()
                json.put("user_id", params[0])
                json.put("date", params[1])
                json.put("start_time", params[2])
                json.put("end_time", params[3])
                json.put("total_hours", params[4])
                json.put("remarks", params[5])

                result = con.sendHttpPostjson(
                        Appconstands.Domin + "permission.php",
                        json
                )
                Log.e("input", json.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("input", e.toString())

            }
            return result
        }

        override fun onPostExecute(resp:String?) {

            try {
                Log.i("tabresp", resp + "")
                pDialo.dismiss()
                if (resp != null) {
                    val json = JSONArray(resp)
                    val obj1 = json.getJSONObject(0)
                    if (obj1.getString("Status") == "Success") {
                        pDialo!!.dismiss()
                        toast("Permission Added Successfully.")
                        finish()

                    } else {
                        pDialo.dismiss()
                        Toast.makeText(activity, obj1.getString("Response"), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    pDialo.dismiss()

                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("err", e.toString())

            }
        }

    }



    fun toast(msg: String){
        val toast=Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}