package com.elancier.abnaattendance

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elancier.peace_admin.Appconstands
import kotlinx.android.synthetic.main.activity_in_outs_.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class InOuts_Activity : AppCompatActivity() {
    val activity=this
    var today_array=ArrayList<People>()
    var adp:All_Daily_Report_Adapter?=null
    private var mYear = 0
    private  var mMonth:Int = 0
    private  var mDay:Int = 0
    private  var mHour:Int = 0
    private  var mMinute:Int = 0
    private var loading = true
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0

    var start=0
    var limit=10
    var utils:Utils?=null
    var dateextra=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_outs_)
        supportActionBar!!.title = "In Out Reports"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val mLayoutManager: LinearLayoutManager
        mLayoutManager = LinearLayoutManager(this)
        recycler!!.setLayoutManager(mLayoutManager)
        adp = All_Daily_Report_Adapter(activity!!, today_array)
        recycler!!.adapter = adp
        recycler!!.itemAnimator = DefaultItemAnimator()
        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        utils=Utils(this)

        try{
            dateextra=intent.extras!!.getString("date").toString()
        }
        catch (e:java.lang.Exception){

        }

        if(dateextra.isEmpty()) {
            val format = SimpleDateFormat("dd-MM-yyyy")
            val frdt = format.format(c.time)
            (c.set(Calendar.DAY_OF_MONTH, 1))
            val todts = format.format(c.time)

            textView3.setText(todts)
            textView4.setText(frdt)
        }
        else{
            textView3.setText(dateextra)
            textView4.setText(dateextra)
        }

        fab1.visibility=View.INVISIBLE

        Get_todayreport().execute(utils!!.getid(),textView3.text.toString(),textView4.text.toString(),start.toString(),
                limit.toString())

        fromdt.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth -> textView3.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)

                    if(textView4.text.toString()!="To Date"){

                        today_array.clear()
                        start=0
                        limit=10
                        Get_todayreport().execute(utils!!.getid(),textView3.text.toString(),textView4.text.toString(),start.toString(),
                                limit.toString())
                    }
                },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.show()
        }

            todt.setOnClickListener {
                val datePickerDialog = DatePickerDialog(
                    this,
                    { view, year, monthOfYear, dayOfMonth -> textView4.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        today_array.clear()
                        start=0
                        limit=10
                        Get_todayreport().execute(utils!!.getid(),textView3.text.toString(),textView4.text.toString(),start.toString(),
                                limit.toString())

                    },
                    mYear,
                    mMonth,
                    mDay
                )
                datePickerDialog.show()
            }


        recycler!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount()
                    totalItemCount = mLayoutManager.getItemCount()
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
                    Log.v("visibleItemCount",visibleItemCount.toString())
                    Log.v("pastVisiblesItems", pastVisiblesItems.toString())
                    Log.v("totalItemCount", totalItemCount.toString())
                    Log.v("loading", loading.toString())

                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            Log.v("...", "Last Item Wow!")
                            Log.v("visibleItemCount", visibleItemCount.toString())
                            Log.v("totalItemCount", totalItemCount.toString())
                            progressBar7.visibility = View.VISIBLE
                            start = totalItemCount
                            //limit=totalItemCount+totalItemCount;
                            Log.v("start", start.toString())
                            Log.v("limit", limit.toString())
                            //today_array.clear()
                            Get_todayreport().execute(utils!!.getid(),textView3.text.toString(),textView4.text.toString(),start.toString(),
                                    limit.toString())
                        }
                    }
                }
            }
        })

    }
    inner class Get_todayreport :
        AsyncTask<String?, Void?, String?>() {
        internal lateinit var pDialo : ProgressDialog
        //var statusval=""

        override fun onPreExecute() {
            // array.clear()
            //today_array.clear()
           /* pDialo = ProgressDialog(activity);
            pDialo.setMessage("Please wait...");
            pDialo.setIndeterminate(false);
            pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialo.setCancelable(false);
            //pDialo.setMax(3)
            pDialo.show()*/

            if(start==0) {
                recycler.visibility=View.INVISIBLE
                shimmer_view_container.visibility = View.VISIBLE
                shimmer_view_container.startShimmer()

            }
            if(start!=0){
                progressBar7.visibility = View.VISIBLE
            }

        }

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params:String?):String? {
            var result: String? = null
            val con =
                Connection()
            //statusval= status.toString()
            try {
                println("devid" + params.get(0))
                println("token" + params.get(1))
                val deviceId = Settings.Secure.getString(
                    activity!!.getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )
                val json= JSONObject()
                json.put("user_id", params[0])
                json.put("from", params[1])
                json.put("to", params[2])
                json.put("start", params[3])
                json.put("skip", params[4])

                result = con.sendHttpPostjson(
                    Appconstands.Domin + "inouts.php",
                    json
                )
                Log.e("input", json.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("input", e.toString())

            }
            return result
        }

        override fun onPostExecute(resp: String?) {

            try {
                Log.i("tabresp", resp + "")
               // pDialo.dismiss()
                if (resp != null) {
                    val json = JSONArray(resp)
                    val obj1 = json.getJSONObject(0)
                    if (obj1.getString("Status") == "Success") {
                        //pDialo!!.dismiss()
                        val jsonarr=obj1.getJSONArray("Response")

                        for(i in 0 until jsonarr.length()){
                            val data=People()
                            data.username=utils!!.getname()
                            data.time=jsonarr.getJSONObject(i).getString("time")
                            data.date=jsonarr.getJSONObject(i).getString("date")
                            data.print_user=jsonarr.getJSONObject(i).getString("remarks")
                            data.logout=jsonarr.getJSONObject(i).getString("type")
                            data.image=jsonarr.getJSONObject(i).getString("image")
                            today_array.add(data)
                        }
                        nodata.visibility = View.GONE
                        recycler.visibility = View.VISIBLE
                        shimmer_view_container.visibility = View.GONE
                        shimmer_view_container.stopShimmer()
                        progressBar7.visibility = View.GONE

                        adp = All_Daily_Report_Adapter(activity!!, today_array)
                        recycler!!.adapter = adp
                        if(start!=0) {
                            try {
                                recycler.smoothScrollToPosition(start + 1)
                            }
                            catch(e:Exception){

                            }
                        }

                    } else {
                       // pDialo.dismiss()
                        shimmer_view_container.visibility= View.GONE
                        shimmer_view_container.stopShimmer()
                        nodata.visibility=View.VISIBLE
                        progressBar7.visibility = View.GONE

                        recycler.visibility=View.INVISIBLE
                        Toast.makeText(activity, obj1.getString("Response"), Toast.LENGTH_SHORT).show()
                    }
                } else {
                   // pDialo.dismiss()
                    shimmer_view_container.visibility= View.GONE
                    shimmer_view_container.stopShimmer()
                    nodata.visibility=View.VISIBLE
                    progressBar7.visibility = View.GONE
                    recycler.visibility=View.INVISIBLE

                }
            } catch (e: Exception) {
                nodata.visibility=View.VISIBLE
                nodata.setText(e.toString())
                recycler.visibility=View.INVISIBLE
                shimmer_view_container.visibility= View.GONE
                shimmer_view_container.stopShimmer()
                progressBar7.visibility = View.GONE

                e.printStackTrace()
                Log.e("err", e.toString())

            }
        }
    }
    override fun onOptionsItemSelected(item:MenuItem):Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}