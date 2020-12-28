package com.elancier.abnaattendance

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elancier.peace_admin.Appconstands
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_login_card_overlap.*
import org.json.JSONArray
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private val mTextMessage: TextView? = null
    private var navigation: BottomNavigationView? = null
    private val search_bar: View? = null
    val activity=this
    var utils:Utils?=null
    internal lateinit var pref  : SharedPreferences
    internal lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_card_overlap)
        pref = applicationContext.getSharedPreferences("MyPref", 0)
        editor = pref.edit()
        utils=Utils(this)

        if(utils!!.login()){
            startActivity(Intent(activity,MainActivity::class.java))
            finish()
        }

        else{

        }

        signin.setOnClickListener {
            if(uname!!.text.toString().isNotEmpty()&&
               pass!!.text.toString().isNotEmpty())
            {
                UpdateInfoTask().execute(uname.text.toString().trim(),pass.text.toString().trim())
            }
            else{
                Toast.makeText(applicationContext,"Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

       /* signin.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }*/
    }
    inner class UpdateInfoTask :
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
                        activity.getContentResolver(),
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
                        val acc_name=arr.getJSONObject(0).getString("acc_name")
                        //val total_present=arr.getJSONObject(0).getString("total_present")
                        val total_absent=arr.getJSONObject(0).getString("total_absent")
                        val absent_salary=arr.getJSONObject(0).getString("absent_salary")
                        val total_late=arr.getJSONObject(0).getString("total_late")
                        val total_permission=arr.getJSONObject(0).getString("total_permission")

                        val acc_no=arr.getJSONObject(0).getString("acc_no")
                        val bank_name=arr.getJSONObject(0).getString("bank_name")
                        val branch_name=arr.getJSONObject(0).getString("branch_name")
                        val ifsc_code=arr.getJSONObject(0).getString("ifsc_code")
                        utils!!.setUser(id,name,mobile,email,city,state,image,fixed_salary,
                                current_salary,total_present,address,acc_name,acc_no,bank_name,branch_name,
                                ifsc_code,uname.text.toString(),pass.text.toString(),total_present,total_absent,
                                total_late,total_permission,absent_salary)

                        utils!!.setLogin(true)
                        startActivity(Intent(activity,MainActivity::class.java))
                        finish()
                    } else {
                        pDialo.dismiss()
                        Toast.makeText(activity, obj1.getString("Response") , Toast.LENGTH_SHORT).show()
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

    }