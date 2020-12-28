package com.elancier.abnaattendance

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elancier.peace_admin.Appconstands
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.activity_in_outs_.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Report_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Report_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var utils:Utils?=null
    var all_array=ArrayList<People>()
    var all_array1=ArrayList<People>()
    var present_array=ArrayList<People>()
    var absent_array=ArrayList<People>()
    var permission_array=ArrayList<People>()
    var adp:All_Report_Adapter?=null
    var adp1:Permission_Report_Adapter?=null
    var recycler:RecyclerView?=null
    var recycler1:RecyclerView?=null
    var all: TextView?=null
    var nodata: TextView?=null
    var present:TextView?=null
    var absent:TextView?=null
    var permission:TextView?=null
    var shimmer:ShimmerFrameLayout?=null
    private var mYear = 0
    private  var mMonth:Int = 0
    private  var mDay:Int = 0
    var limit=10
    private var loading = true
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    var start=0
    var permissionclick=""
    var progressBar7:ProgressBar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container:ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.reports_fragment, container, false)
        recycler=view.findViewById(R.id.recycler);
        recycler1=view.findViewById(R.id.recycler1);
        all=view.findViewById(R.id.all)
        present=view.findViewById(R.id.present)
        absent=view.findViewById(R.id.absent)
        nodata=view.findViewById(R.id.textView20)
        progressBar7=view.findViewById(R.id.progressBar7)
        val fromdt=view.findViewById<TextView>(R.id.textView3)
        val todt=view.findViewById<TextView>(R.id.textView4)
        shimmer=view.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
        permission=view.findViewById(R.id.permission)
        val mLayoutManager: LinearLayoutManager
        mLayoutManager = LinearLayoutManager(activity!!)
        recycler!!.setLayoutManager(mLayoutManager)

        val mLayoutManager1: LinearLayoutManager
        mLayoutManager1 = LinearLayoutManager(activity!!)
        recycler1!!.setLayoutManager(mLayoutManager1)
        //recycler!!.setLayoutManager(LinearLayoutManager(activity));
        adp = All_Report_Adapter(activity!!,all_array)
        recycler!!.adapter = adp

        adp1 = Permission_Report_Adapter(activity!!,all_array1)
        recycler1!!.adapter = adp1

        utils=Utils(activity!!)
        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        utils=Utils(activity!!)

        val format= SimpleDateFormat("dd-MM-yyyy")
        val frdt=format.format(c.time)
        (c.set(Calendar.DAY_OF_MONTH,1))
        val todts=format.format(c.time)
        fromdt.setText(todts)
        todt.setText(frdt)



        present!!.setOnClickListener {
            permissionclick=""
            all!!.setBackgroundColor(activity!!.resources.getColor(R.color.white))
            present!!.setBackgroundColor(activity!!.resources.getColor(R.color.blue_50))
            absent!!.setBackgroundColor(activity!!.resources.getColor(R.color.white))
            permission!!.setBackgroundColor(activity!!.resources.getColor(R.color.white))

            if(present_array!!.isNotEmpty()) {
                adp = All_Report_Adapter(activity!!, present_array)
                recycler!!.adapter = adp
                recycler!!.visibility=View.VISIBLE
                recycler1!!.visibility=View.INVISIBLE
            }
            else{
                all_array.clear()
                Get_todayreport().execute(utils!!.getid(),fromdt.text.toString(),todt.text.toString(),start.toString(),
                        limit.toString())
            }
        }
        all!!.setOnClickListener {
            permissionclick=""

            if(all_array.isNotEmpty()){
                recycler!!.visibility=View.VISIBLE
                recycler1!!.visibility=View.INVISIBLE
                adp = All_Report_Adapter(activity!!,all_array)
                recycler!!.adapter = adp
            }
            else{
                all_array.clear()
                Get_todayreport().execute(utils!!.getid(),fromdt.text.toString(),todt.text.toString(),start.toString(),
                        limit.toString())
            }

            all!!.setBackgroundColor(activity!!.resources.getColor(R.color.blue_50))
            present!!.setBackgroundColor(activity!!.resources.getColor(R.color.white))
            absent!!.setBackgroundColor(activity!!.resources.getColor(R.color.white))
            permission!!.setBackgroundColor(activity!!.resources.getColor(R.color.white))
        }

        absent!!.setOnClickListener {
            permissionclick=""
            all!!.setBackgroundColor(activity!!.resources.getColor(R.color.white))
            absent!!.setBackgroundColor(activity!!.resources.getColor(R.color.blue_50))
            present!!.setBackgroundColor(activity!!.resources.getColor(R.color.white))
            permission!!.setBackgroundColor(activity!!.resources.getColor(R.color.white))

            if(absent_array!!.isNotEmpty()) {
                adp = All_Report_Adapter(activity!!, absent_array)
                recycler!!.adapter = adp
                recycler!!.visibility = View.VISIBLE
                recycler1!!.visibility = View.INVISIBLE
            }
            else{
                all_array.clear()
                Get_todayreport().execute(utils!!.getid(),fromdt.text.toString(),todt.text.toString(),start.toString(),
                        limit.toString())
            }


        }
        permission!!.setOnClickListener {
            permissionclick="true"
            if(all_array1.isNotEmpty()){
                recycler1!!.visibility=View.VISIBLE
                recycler!!.visibility=View.INVISIBLE

            }
            else{
                all_array1.clear()
                Get_Permsions().execute(utils!!.getid(),fromdt.text.toString(),todt.text.toString(),start.toString(),
                        limit.toString())
            }
            all!!.setBackgroundColor(activity!!.resources.getColor(R.color.white))
            permission!!.setBackgroundColor(activity!!.resources.getColor(R.color.blue_50))
            present!!.setBackgroundColor(activity!!.resources.getColor(R.color.white))
            absent!!.setBackgroundColor(activity!!.resources.getColor(R.color.white))
            start=0
            limit=10
        }

        fromdt.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                activity!!,
                { view, year, monthOfYear, dayOfMonth -> textView3.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)

                    if(textView4.text.toString()!="To Date"&&permissionclick.isEmpty()){
                        all_array.clear()
                        all_array1.clear()

                        Get_todayreport().execute(utils!!.getid(),fromdt.text.toString(),todt.text.toString(),start.toString(),
                        limit.toString())
                    }
                    else if(textView4.text.toString()!="To Date"&&permissionclick=="true"){
                        all_array1.clear()
                        all_array.clear()
                        present_array.clear()
                        absent_array.clear()

                        start=0
                        limit=10
                        Get_Permsions().execute(utils!!.getid(),fromdt.text.toString(),todt.text.toString(),start.toString(),
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
                activity!!,
                { view, year, monthOfYear, dayOfMonth -> textView4.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)

                    if(textView3.text.toString()!="From Date"&&permissionclick.isEmpty()) {
                        all_array.clear()
                        all_array1.clear()
                        present_array.clear()
                        absent_array.clear()
                        Get_todayreport().execute(utils!!.getid(), fromdt.text.toString(), todt.text.toString(), start.toString(),
                                limit.toString())

                    }
                    else if(textView3.text.toString()!="From Date"&&permissionclick=="true"){
                        all_array1.clear()
                        all_array.clear()
                        present_array.clear()
                        absent_array.clear()
                        start=0
                        limit=10
                        Get_Permsions().execute(utils!!.getid(),fromdt.text.toString(),todt.text.toString(),start.toString(),
                                limit.toString())
                    }

                },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.show()
        }

        Get_todayreport().execute(utils!!.getid(),fromdt.text.toString(),todt.text.toString())


        recycler1!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//                            progressBar7.visibility = View.VISIBLE
                            start = totalItemCount
                            //limit=totalItemCount+totalItemCount;
                            Log.v("start", start.toString())
                            Log.v("limit", limit.toString())
                            //today_array.clear()
                            Get_Permsions().execute(utils!!.getid(),fromdt.text.toString(),todt.text.toString(),start.toString(),
                                    limit.toString())
                        }
                    }
                }
            }
        })


        return view
    }

    inner class Get_todayreport :
            AsyncTask<String?, Void?, String?>() {
        internal lateinit var pDialo : ProgressDialog
        //var statusval=""

        override fun onPreExecute() {
            // array.clear()
            all_array.clear()
            present_array.clear()
            absent_array.clear()
            shimmer!!.visibility=View.VISIBLE
            shimmer!!.startShimmer()
            recycler!!.visibility=View.INVISIBLE
            recycler1!!.visibility=View.INVISIBLE
           /* pDialo = ProgressDialog(activity);
            pDialo.setMessage("Please wait...");
            pDialo.setIndeterminate(false);
            pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialo.setCancelable(false);
            //pDialo.setMax(3)
            pDialo.show()*/
        }

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: String?): String? {
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


                result = con.sendHttpPostjson(
                        Appconstands.Domin + "all_signin.php",
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
               // pDialo.dismiss()
                if (resp != null) {
                    val json = JSONArray(resp)
                    val obj1 = json.getJSONObject(0)
                    if (obj1.getString("Status") == "Success") {
                       // pDialo!!.dismiss()
                        present!!.setText("Present\n"+obj1.getJSONObject("Response").getString("total_presents"))
                        absent!!.setText("Absent\n"+obj1.getJSONObject("Response").getString("total_absents"))
                        all!!.setText("All\n"+obj1.getJSONObject("Response").getString("total_days"))
                        permission!!.setText("Permission\n"+obj1.getJSONObject("Response").getString("total_permissions"))

                        val jsonarr=obj1.getJSONObject("Response").getJSONArray("signin")

                        for(i in 0 until jsonarr.length()){
                            val data=People()
                            data.username=utils!!.getname()
                            data.time=jsonarr.getJSONObject(i).getString("signin_time")
                            data.date=jsonarr.getJSONObject(i).getString("date")
                            data.print_user=jsonarr.getJSONObject(i).getString("signout_time")
                            data.logout=jsonarr.getJSONObject(i).getString("type")
                            data.image=""
                            if(data.logout=="Present"){
                                present_array.add(data)
                            }
                            else if(data.logout=="Absent"){
                                absent_array.add(data)

                            }
                            all_array.add(data)
                        }
                        shimmer!!.visibility=View.GONE
                        shimmer!!.stopShimmer()
                        nodata!!.visibility=View.GONE
                        recycler!!.visibility=View.VISIBLE
                        recycler1!!.visibility=View.INVISIBLE
                        adp = All_Report_Adapter(activity!!,all_array)
                        recycler!!.adapter = adp


                    } else {
                        shimmer!!.visibility=View.GONE
                        shimmer!!.stopShimmer()
                        //pDialo.dismiss()
                        nodata!!.visibility=View.VISIBLE
                        recycler!!.visibility=View.INVISIBLE
                        Toast.makeText(activity, obj1.getString("Response"), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    shimmer!!.visibility=View.GONE
                    shimmer!!.stopShimmer()
                    nodata!!.visibility=View.VISIBLE
                    recycler!!.visibility=View.INVISIBLE
                   // pDialo.dismiss()

                }
            } catch (e: Exception) {
                shimmer!!.visibility=View.GONE
                shimmer!!.stopShimmer()
                e.printStackTrace()
                nodata!!.visibility=View.VISIBLE
                recycler!!.visibility=View.INVISIBLE
                Log.e("err", e.toString())

            }
        }
    }


    inner class Get_Permsions :
            AsyncTask<String?, Void?, String?>() {
        internal lateinit var pDialo : ProgressDialog
        //var statusval=""

        override fun onPreExecute() {
            // array.clear()
            /*present_array.clear()
            absent_array.clear()
            permission_array.clear()*/
            if(start==0) {
               // all_array1.clear()
                recycler1!!.visibility=View.INVISIBLE
                recycler!!.visibility=View.INVISIBLE
                shimmer_view_container.visibility = View.VISIBLE
                shimmer_view_container.startShimmer()
            }
            if(start!=0) {
                progressBar7!!.visibility = View.VISIBLE
            }
            /* pDialo = ProgressDialog(activity);
             pDialo.setMessage("Please wait...");
             pDialo.setIndeterminate(false);
             pDialo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             pDialo.setCancelable(false);
             //pDialo.setMax(3)
             pDialo.show()*/
        }

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: String?): String? {
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
                        Appconstands.Domin + "permission_list.php",
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
                // pDialo.dismiss()
                if (resp != null) {
                    val json = JSONArray(resp)
                    val obj1 = json.getJSONObject(0)
                    if (obj1.getString("Status") == "Success") {
                        // pDialo!!.dismiss()
                       // present!!.setText("Present\n"+obj1.getJSONObject("Response").getString("total_presents"))
                       // absent!!.setText("Absent\n"+obj1.getJSONObject("Response").getString("total_absents"))
                       // all!!.setText("All\n"+obj1.getJSONObject("Response").getString("total_days"))
                       // permission!!.setText("Permission\n"+obj1.getJSONObject("Response").getString("total_permissions"))
                        val jsonarr=obj1.getJSONArray("Response")

                        permission!!.setText("Permission\n"+jsonarr.length())
                        for(i in 0 until jsonarr.length()){
                            val data=People()
                            data.username=utils!!.getname()
                            data.time=jsonarr.getJSONObject(i).getString("start_time")
                            data.date=jsonarr.getJSONObject(i).getString("date")
                            data.print_user=jsonarr.getJSONObject(i).getString("remarks")
                            data.logout=jsonarr.getJSONObject(i).getString("end_time")

                            val splits=jsonarr.getJSONObject(i).getString("total_hours")
                            val tothrs=splits.split(",")
                            data.image=tothrs[0]
                            data.ip_address=tothrs[1]

                            all_array1.add(data)
                        }
                        shimmer!!.visibility=View.GONE
                        shimmer!!.stopShimmer()
                        recycler1!!.visibility=View.VISIBLE
                        nodata!!.visibility=View.GONE
                        progressBar7!!.visibility=View.GONE
                        adp1!!.notifyDataSetChanged()


                    } else {
                        shimmer!!.visibility=View.GONE
                        shimmer!!.stopShimmer()
                        progressBar7!!.visibility=View.GONE

                        recycler1!!.visibility=View.INVISIBLE
                        //recycler1!!.visibility=View.VISIBLE
                        nodata!!.visibility=View.VISIBLE
                        //pDialo.dismiss()
                        Toast.makeText(activity, obj1.getString("Response"), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    shimmer!!.visibility=View.GONE
                    shimmer!!.stopShimmer()
                    progressBar7!!.visibility=View.GONE

                    recycler1!!.visibility=View.INVISIBLE
                    nodata!!.visibility=View.VISIBLE

                    // pDialo.dismiss()

                }
            } catch (e: Exception) {
                shimmer!!.visibility=View.GONE
                shimmer!!.stopShimmer()
                progressBar7!!.visibility=View.GONE

                recycler1!!.visibility=View.INVISIBLE
                nodata!!.visibility=View.VISIBLE

                e.printStackTrace()
                Log.e("err", e.toString())

            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Report_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Report_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}