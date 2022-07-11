package com.elancier.abnaattendance

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SyncRequest
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
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
        arr.add("Select Leave")
        arr.add("Quarter day")
        arr.add("Half day")
        arr.add("3/4th day")
        arr.add("Full day")
        val customAdapter = CustomeSpinner(activity, arr as java.util.ArrayList<String>)
        ddselect.adapter = customAdapter


        pdate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if(s!!.length>0) {
                    if (s.toString().equals("1") || s.toString().equals("0")) {
                        tocard.visibility = View.GONE
                    } else {
                        tocard.visibility = View.VISIBLE

                    }
                }
                else{
                    tocard.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, star: Int, before: Int, count: Int) {

            }
        })





        imageButton.setOnClickListener {
            finish()
        }

        signin.setOnClickListener {
            if(pdate.text.toString().isNotEmpty()&&ftime.text.toString().isNotEmpty()&&
                ddselect.selectedItemPosition!=0){
                if(tocard.visibility==View.VISIBLE){
                    if(todates.text.toString().isNotEmpty()){
                        UpdateInfoTask().execute(utils!!.getid(),ftime.text.toString(),
                            todates.text.toString(), ddselect.selectedItem.toString(),
                            reason.text.toString(),pdate.text.toString())
                    }
                    else{
                        if(todates.text.toString().isEmpty()){
                            todates.setError("Required field")
                        }
                    }
                }
                else{
                    UpdateInfoTask().execute(utils!!.getid(),ftime.text.toString(),
                        todates.text.toString(), ddselect.selectedItem.toString(),
                        reason.text.toString(),pdate.text.toString())
                }

            }
            else{
                if(pdate.text.toString().isEmpty()){
                    pdate.setError("Required field")
                }
                if(ftime.text.toString().isEmpty()){
                    ftime.setError("Required field")
                }
                if(ddselect.selectedItemPosition==0){
                    toast("Please select leave")
                }

            }
        }

        ftime.setOnClickListener {

            if(pdate.text.toString().isNotEmpty()) {
                val datePickerDialog = DatePickerDialog(
                    this,
                    { view, year, monthOfYear, dayOfMonth -> ftime.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) },
                    mYear,
                    mMonth,
                    mDay
                )
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show()
            }
            else{
                toast("Please fill days")
                pdate.setError("Required field")

            }
        }


        todates.setOnClickListener {
            if(pdate.text.toString().isNotEmpty()) {
                val datePickerDialog = DatePickerDialog(
                    this,
                    { view, year, monthOfYear, dayOfMonth -> todates.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) },
                    mYear,
                    mMonth,
                    mDay
                )
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show()
            }
            else{
                toast("Please fill days")
                pdate.setError("Required field")
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
                json.put("from_date", params[1])
                json.put("to_date", params[2])
                json.put("type", params[3])
                json.put("reason", params[4])
                json.put("types", "add")
                json.put("days", params[5])

                result = con.sendHttpPostjson(
                        Appconstands.Domin + "leaveRequest.php",
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
                        toast("Leave applied Successfully.")
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