package com.elancier.abnaattendance

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elancier.peace_admin.Appconstands
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.LocationRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_login_card_overlap.*
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
 * Use the [Homefragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Homefragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    internal var locationManager: LocationManager? = null
    internal var gpslisten = ""
    internal var lat: Double = 0.toDouble()
    internal var longi:Double = 0.toDouble()
     var gps:GPSData?= null
     var statusval=""
    var locationmanager:LocationManager? = null
    var isGPS = false;
    var loaded=false
    var utils:Utils?=null
    internal lateinit var locationRequest:LocationRequest
    lateinit var pop:AlertDialog
    lateinit var popup:AlertDialog
    internal lateinit var byteArray:ByteArray
    lateinit var imageone:ImageView
    internal lateinit var imagecode: String
    internal lateinit var util:Utils
    internal lateinit var pref  : SharedPreferences
    internal lateinit var editor: SharedPreferences.Editor
    var today_array=ArrayList<People>()
    var recycler:RecyclerView?=null
    var today_dt:TextView?=null
    var today_no:TextView?=null
    var adp:Daily_Report_Adapter?=null
    var Shimmer:ShimmerFrameLayout?=null
    var pDialog:ProgressDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun getLocation() {
        try {
            println("locat")
            locationmanager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {

                return
            }
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            //locationmanager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, activity)
            locationmanager!!.requestSingleUpdate(
                criteria,
                object : android.location.LocationListener {
                    override fun onLocationChanged(location:Location) {
                        println("single_location : " + location.latitude + " , " + location.longitude)
                        lat = location.latitude
                        longi = location.longitude

                        if (lat>0 && longi>0) {
                            pDialog!!.dismiss()
                            if(statusval=="in"){
                                incheck()
                            }
                            else if(statusval=="out"){
                                outcheck()
                            }
                        }
                        else
                        {
                            getLocation()
                        }
                    }

                    override fun onStatusChanged(
                        provider: String,
                        status: Int,
                        extras: Bundle
                    ) {

                    }

                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                },
                null
            )
            // Log.e(“Network”, “Network”);
            if (locationmanager != null) {
                val location = locationmanager!!
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location != null) {
                    println("lastknown : " + location.latitude + " , " + location.longitude)

                }
            }
        } catch (e:SecurityException) {
            e.printStackTrace()
            println("error : " + e.printStackTrace())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container:ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        util=Utils(activity!!)
        val view= inflater.inflate(R.layout.activity_dashboard_wallet, container, false)
        val login=view.findViewById<CardView>(R.id.c1);
        val details=view.findViewById<TextView>(R.id.textView);
        val logout=view.findViewById<CardView>(R.id.c2);
        val incard=view.findViewById<CardView>(R.id.incard)
        val permssioncard=view.findViewById<CardView>(R.id.permssioncard)
        val cur_salary=view.findViewById<TextView>(R.id.cur_salary)
         today_dt=view.findViewById<TextView>(R.id.today_dt)
        today_no=view.findViewById<TextView>(R.id.today_noreport)
         recycler=view.findViewById<RecyclerView>(R.id.recycler)
        Shimmer=view.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
        cur_salary.setText(Appconstands.rupees + util.getcurrent_salary())
        val fab=view.findViewById<FloatingActionButton>(R.id.fab);
        val fabs=view.findViewById<FloatingActionButton>(R.id.fabs);
        gps=GPSData(activity!!)
        gps!!.getLocation()
        utils=Utils(activity!!)
        pDialog = ProgressDialog(activity);

        recycler!!.setLayoutManager(LinearLayoutManager(activity));
        adp = Daily_Report_Adapter(activity!!,today_array)
        recycler!!.adapter = adp

        fab.setOnClickListener {
            startActivity(Intent(activity!!, Permisiion_Activity::class.java))
        }

        fabs.setOnClickListener {
            startActivity(Intent(activity!!,InOuts_Activity::class.java))
        }

        incard.setOnClickListener {
            startActivity(Intent(activity!!,InOuts_Activity::class.java))
        }

        permssioncard.setOnClickListener {
            startActivity(Intent(activity!!, Permisiion_Activity::class.java))
        }

       // statusCheck()

        if (ContextCompat.checkSelfPermission(
                        activity!!,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            // val mLocationManager =  getSystemService(LOCATION_SERVICE) as (LocationManager);
            // mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, this);
            locationManager =
                    activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000,
                    10F,
                    locationListenerGPS
            )

        }

        getLocation()


        details.setOnClickListener {
            startActivity(Intent(activity!!, Salary_Details::class.java))
        }

        login.setOnClickListener {

            statusval="in"

            if(CheckingPermissionIsEnabledOrNot()){

                if(lat>0&&longi>0) {
                    incheck()
                }
                else{

                    pDialog!!.setMessage("Please wait...");
                    pDialog!!.setIndeterminate(false);
                    pDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pDialog!!.setCancelable(false);
                    //pDialo.setMax(3)
                    pDialog!!.show()
                    if (ContextCompat.checkSelfPermission(
                                    activity!!,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // val mLocationManager =  getSystemService(LOCATION_SERVICE) as (LocationManager);
                        // mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, this);
                        locationManager =
                                activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        locationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                2000,
                                10F,
                                locationListenerGPS
                        )

                    }

                    //statusCheck()
                        //getLocation()
                    Toast.makeText(activity, "$lat$longi", Toast.LENGTH_SHORT).show()
                }




            }
            else{
                RequestMultiplePermission()
            }

        }

        logout.setOnClickListener {
            statusval="out"

            if(CheckingPermissionIsEnabledOrNot()){
                if(lat>0&&longi>0) {
                    outcheck()
                }
                else{

                    pDialog!!.setMessage("Please wait...");
                    pDialog!!.setIndeterminate(false);
                    pDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pDialog!!.setCancelable(false);
                    //pDialo.setMax(3)
                    pDialog!!.show()
                    if (ContextCompat.checkSelfPermission(
                                    activity!!,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // val mLocationManager =  getSystemService(LOCATION_SERVICE) as (LocationManager);
                        // mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, this);
                        locationManager =
                                activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        locationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                2000,
                                10F,
                                locationListenerGPS
                        )

                    }

                    //statusCheck()
                    //getLocation()
                    Toast.makeText(activity, "$lat$longi", Toast.LENGTH_SHORT).show()
                }




            }
            else{
                RequestMultiplePermission()
            }

        }

        val calender=Calendar.getInstance()
        val format=SimpleDateFormat("dd-MM-yyyy")
        val date=format.format(calender.time)
        println("date"+date)
        println("id"+utils!!.getid())
        today_dt!!.setText("Today ($date)")

        Get_todayreport().execute(utils!!.getid(),date,date,"0","10")
        return view;

    }

    fun outcheck(){
        if (CheckingPermissionIsEnabledOrNot()) {

            if((lat>0F||longi>0F)) {
                (activity as MainActivity).cam(statusval)
            }
            else{

                //toast("Unable to get location.Try Again")
            }

        } else {
            RequestMultiplePermission()
        }
    }


    fun incheck(){
        if (CheckingPermissionIsEnabledOrNot()) {

            if((lat>0F||longi>0F)) {
                (activity as MainActivity).cam(statusval)
            }
            else{

                //toast("Unable to get location.Try Again")
            }

        } else {
            RequestMultiplePermission()
        }
    }

    private fun checkPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
            !== PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            !== PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            false
        } else true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity!!, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            200
        )
    }


    fun statusCheck() {
        val manager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        println("net : " + manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        println("gps : " + manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)&&!manager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            ))
        {
            println("perform")
            GpsUtils(activity!!).turnGPSOn(object : GpsUtils.onGpsListener {
                override fun gpsStatus(isGPSEnable: Boolean) {
                    // turn on GPS
                    isGPS = isGPSEnable
                    println("network & gps else " + isGPSEnable)
                    getLocation()
                }
            })

        }else{
            println("else : ")

            if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            {
                getLocation()

            }else if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                getLocation()

            }else {
                GpsUtils(activity!!).turnGPSOn(object : GpsUtils.onGpsListener {
                    override fun gpsStatus(isGPSEnable: Boolean) {
                        // turn on GPS
                        isGPS = isGPSEnable
                        println("network else " + isGPSEnable)
                        getLocation()
                    }
                })
            }
        }
    }

    var locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: android.location.Location) {
            val latitude = location.latitude
            val longitude = location.longitude
            val msg = "New Latitude: " + latitude + "New Longitude: " + longitude
            println("msglat_lang" + msg)
            lat=location.latitude
            longi=location.longitude
            //Toast.makeText(this@HomeActivity, msg, Toast.LENGTH_LONG).show()
            //location_shimmer.stopShimmer()
            //location_shimmer.visibility=View.GONE
            // location_layout.visibility=View.VISIBLE
            // getCompleteAddressString(location)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

            //getLocation()
        }

        override fun onProviderEnabled(provider: String) {
            getLocation()

        }

        override fun onProviderDisabled(provider: String) {
            statusCheck()
        }
    }


    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity!!)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Homefragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1:String, param2:String) =
            Homefragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun myActions() {
        Log.v("inside",statusval)
        if(statusval=="in") {
            UpdateInfoTask().execute(
                utils!!.getid(),
                "signin",
                lat.toString(),
                longi.toString(),
                (activity as MainActivity).imagecode,
                (activity as MainActivity).reason
            )
        }

        else {
            UpdateInfoTask().execute(
                utils!!.getid(),
                "signout",
                lat.toString(),
                longi.toString(),
                (activity as MainActivity).imagecode,
                (activity as MainActivity).reason
            )
        }
    }

    fun toast(msg: String){
        val toast=Toast.makeText(context!!, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
    fun CheckingPermissionIsEnabledOrNot(): Boolean {

        val INTERNET = ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.INTERNET)
        val ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(
            activity!!,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        )
        //val ACCESS_NOTIFICATION_POLICY = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        val ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(
            activity!!,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(
            activity!!,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val CAMERA = ContextCompat.checkSelfPermission(
            activity!!,
            android.Manifest.permission.CAMERA
        )
        val STORAGE_READ = ContextCompat.checkSelfPermission(
                activity!!,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val STORAGE_WRITE = ContextCompat.checkSelfPermission(
                activity!!,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        return INTERNET == PackageManager.PERMISSION_GRANTED &&
                ACCESS_NETWORK_STATE == PackageManager.PERMISSION_GRANTED &&
                //ACCESS_NOTIFICATION_POLICY == PackageManager.PERMISSION_GRANTED &&
                ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED &&
                CAMERA == PackageManager.PERMISSION_GRANTED &&
                STORAGE_READ == PackageManager.PERMISSION_GRANTED &&
                STORAGE_WRITE == PackageManager.PERMISSION_GRANTED &&
                ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED //&&
        //CALL_PHONE == PackageManager.PERMISSION_GRANTED
    }
    fun RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(
            activity!!, arrayOf<String>
                (
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //android.Manifest.permission.CALL_PHONE
            ), Appconstands.RequestPermissionCode
        )


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {

            Appconstands.RequestPermissionCode ->

                if (grantResults.size > 0) {

                    val INTERNET = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_NETWORK_STATE = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    //val ACCESS_NOTIFICATION_POLICY = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_FINE_LOCATION = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val ACCESS_COARSE_LOCATION =
                        grantResults[3] == PackageManager.PERMISSION_GRANTED
                    val CAMERA =
                            grantResults[4] == PackageManager.PERMISSION_GRANTED
                    val READ =
                            grantResults[4] == PackageManager.PERMISSION_GRANTED
                    val WRITE =
                            grantResults[4] == PackageManager.PERMISSION_GRANTED

                    //val CALL_PHONE = grantResults[4] == PackageManager.PERMISSION_GRANTED

                    if (INTERNET && ACCESS_NETWORK_STATE /*&& ACCESS_NOTIFICATION_POLICY*/ && ACCESS_FINE_LOCATION && ACCESS_COARSE_LOCATION && CAMERA && READ && WRITE /*&&CALL_PHONE*/) {
                        if (ContextCompat.checkSelfPermission(
                                activity!!,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            /*val mLocationManager =  getSystemService(LOCATION_SERVICE) as (LocationManager);
                            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);*/
                            getLocation()
                        }
                        //Toast.makeText(this@MainFirstActivity,"Permission Granted", Toast.LENGTH_LONG).show()
                    } else {
                        //Toast.makeText(this@MainFirstActivity,"Permission Denied", Toast.LENGTH_LONG).show()

                    }
                }

        }
    }


    override fun onResume() {
        super.onResume()
        utils=Utils(activity!!)
        LoginInfoTask().execute(utils!!.get_uname(),utils!!.get_pass())
    }


    inner class LoginInfoTask :
            AsyncTask<String?, Void?, String?>() {
        internal lateinit var pDialo : ProgressDialog

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
            try {
                println("devid" + params.get(0))
                println("token" + params.get(1))
                val deviceId = Settings.Secure.getString(
                        activity!!.getContentResolver(),
                        Settings.Secure.ANDROID_ID
                )
                val json= JSONObject()
                json.put("username", params[0])
                json.put("password", params[1])

                result = con.sendHttpPostjson(
                        Appconstands.Domin+"login.php",
                        json
                )
                Log.e("input",json.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("input",e.toString())

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
                        val arr=obj1.getJSONArray("Response")
                        val id=arr.getJSONObject(0).getString("user_id")
                        val name=arr.getJSONObject(0).getString("name")
                        val mobile=arr.getJSONObject(0).getString("mobile")
                        val email=arr.getJSONObject(0).getString("email")
                        val city=arr.getJSONObject(0).getString("city")
                        val state=arr.getJSONObject(0).getString("state")
                        val image=arr.getJSONObject(0).getString("image")
                        val address=arr.getJSONObject(0).getString("address")
                        val fixed_salary=arr.getJSONObject(0).getString("fixed_salary")
                        val current_salary=arr.getJSONObject(0).getString("current_salary")
                        val total_present=arr.getJSONObject(0).getString("total_present")
                        val total_absent=arr.getJSONObject(0).getString("total_absent")
                        val absent_salary=arr.getJSONObject(0).getString("absent_salary")
                        val total_late=arr.getJSONObject(0).getString("total_late")
                        val total_permission=arr.getJSONObject(0).getString("total_permission")
                        val acc_name=arr.getJSONObject(0).getString("acc_name")
                        val acc_no=arr.getJSONObject(0).getString("acc_no")
                        val bank_name=arr.getJSONObject(0).getString("bank_name")
                        val branch_name=arr.getJSONObject(0).getString("branch_name")
                        val ifsc_code=arr.getJSONObject(0).getString("ifsc_code")
                        utils!!.setUser(id,name,mobile,email,city,state,image,fixed_salary,
                                current_salary,total_present,address,acc_name,acc_no,bank_name,branch_name,
                                ifsc_code,utils!!.get_uname().toString(),utils!!.get_pass().toString(),total_present,total_absent,
                                total_late,total_permission,absent_salary)

                        utils!!.setLogin(true)
                        //startActivity(Intent(activity,MainActivity::class.java))
                       // finish()
                    } else {
                        pDialo.dismiss()
                        //Toast.makeText(activity, obj1.getString("Response") , Toast.LENGTH_SHORT).show()
                    }
                } else {
                    pDialo.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("err",e.toString())
            }
        }
    }

    inner class Get_todayreport :
        AsyncTask<String?, Void?, String?>() {
        internal lateinit var pDialo : ProgressDialog
        //var statusval=""

        override fun onPreExecute() {
            // array.clear()
            today_array.clear()
            Shimmer!!.visibility=View.VISIBLE
            recycler!!.visibility=View.GONE
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

        override fun onPostExecute(resp:String?) {

            try {
                Log.i("tabresp", resp + "")
                //pDialo.dismiss()
                if (resp != null) {
                    val json = JSONArray(resp)
                    val obj1 = json.getJSONObject(0)
                    if (obj1.getString("Status") == "Success") {
                       // pDialo!!.dismiss()
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
                        Shimmer!!.visibility=View.GONE
                        recycler!!.visibility=View.VISIBLE
                        today_no!!.visibility=View.GONE
                        adp!!.notifyDataSetChanged()


                    } else {
                        //pDialo.dismiss()
                        Shimmer!!.visibility=View.GONE
                        recycler!!.visibility=View.GONE
                        today_no!!.visibility=View.VISIBLE

                        //Toast.makeText(activity, obj1.getString("Response"), Toast.LENGTH_SHORT).show()
                    }
                } else {
                   // pDialo.dismiss()
                    Shimmer!!.visibility=View.GONE
                    recycler!!.visibility=View.GONE
                    today_no!!.visibility=View.VISIBLE


                }
            } catch (e: Exception) {
                Shimmer!!.visibility=View.GONE
                recycler!!.visibility=View.GONE
                today_no!!.visibility=View.VISIBLE

                e.printStackTrace()
                Log.e("err", e.toString())

            }
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
                println("devid" + params.get(0))
                println("token" + params.get(1))
                val deviceId = Settings.Secure.getString(
                    activity!!.getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )
                val json= JSONObject()
                json.put("user_id", params[0])
                json.put("type", params[1])
                json.put("lat", params[2])
                json.put("lon", params[3])
                json.put("image", params[4])
                json.put("remarks", params[5])

                result = con.sendHttpPostjson(
                    Appconstands.Domin + "signin.php",
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
                pDialo.dismiss()
                if (resp != null) {
                    val json = JSONArray(resp)
                    val obj1 = json.getJSONObject(0)
                    if (obj1.getString("Status") == "Success") {
                        pDialo!!.dismiss()
                        val calender=Calendar.getInstance()
                        val format=SimpleDateFormat("dd-MM-yyyy")
                        val date=format.format(calender.time)
                        println("date"+date)
                        println("id"+utils!!.getid())
                        today_dt!!.setText("Today ($date)")

                        Get_todayreport().execute(utils!!.getid(),date,date,"0","10")
                        if(statusval=="in"){
                                toast("Logged In Successfully")

                            }
                        else{
                            toast("Logged Out Successfully")

                        }

                    } else {
                        pDialo.dismiss()
                        toast(obj1.getString("Response"))
                    }
                } else {
                    pDialo.dismiss()

                }
            } catch (e: Exception) {

                e.printStackTrace()
                toast(e.toString())
                Log.e("err", e.toString())

            }
        }
    }

}