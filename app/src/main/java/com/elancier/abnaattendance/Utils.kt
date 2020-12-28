package com.elancier.abnaattendance

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.preference.PreferenceManager

class Utils(internal var _context: Context) {
    internal var sharedPreferences: SharedPreferences

    val isConnectingToInternet: Boolean
        get() {
            val connectivity =
                _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.allNetworkInfo
                if (info != null)
                    for (i in info.indices)
                        if (info[i].state == NetworkInfo.State.CONNECTED) {
                            return true
                        }

            }
            return false
        }

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
    }



    fun savePreferences(string: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(string, value)
        editor.commit()
    }

    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.commit()
    }

    fun login():Boolean{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getBoolean("login", false)
    }
    fun setLogin(t : Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("login", t)
        editor.commit()
    }
    fun getid():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("id","")
    }
    fun getname():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("name","")
    }
    fun getmobile():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("mobile","")
    }
    fun getemail():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("email","")
    }
    fun getcity():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("city","")
    }
    fun getstate():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("state","")
    }
    fun getaddress():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("address","")
    }
    fun getimage():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("image","")
    }
    fun getfixed_salary():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("fixed_salary","")
    }
    fun getcurrent_salary():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("current_salary","")
    }
    fun gettotal_present():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("total_present","")
    }

    fun getacc_name():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("acc_name","")
    }
    fun get_uname():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("uname","")
    }
    fun get_pass():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("pass","")
    }
    fun getacc_no():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("acc_no","")
    }
    fun getbank_name():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("bank_name","")
    }
    fun getbranch_name():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("branch_name","")
    }
    fun getifsc_code():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("ifsc_code","")
    }
    fun get_total_present():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("total_present","")
    }
    fun get_total_absent():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("total_absent","")
    }
    fun get_total_late():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("total_late","")
    }
    fun get_total_permission():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("total_permission","")
    }
    fun get_absent_salary():String?{
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("absent_salary","")
    }

    fun setUser(id:String,name:String,mobile:String,email:String,
                city:String,state:String,img:String,fxd_slry:String,
                curr_salary:String,tot_present:String,address:String,
                acc_name:String,acc_no:String,bank_name:String,
                branch_name:String,ifsc_code:String,uname:String,pass:String,
                total_present:String, total_absent:String,
                total_late:String, total_permission:String, absent_salary:String){
        val editor = sharedPreferences.edit()
        editor.putString("id", id)
        editor.putString("name", name)
        editor.putString("mobile", mobile)
        editor.putString("email", email)
        editor.putString("city", city)
        editor.putString("state", state)
        editor.putString("image", img)
        editor.putString("address", address)
        editor.putString("fixed_salary", fxd_slry)
        editor.putString("current_salary", curr_salary)
        editor.putString("total_present", tot_present)
        editor.putString("acc_name",acc_name)
        editor.putString("acc_no",acc_no)
        editor.putString("bank_name",bank_name)
        editor.putString("branch_name",branch_name)
        editor.putString("ifsc_code",ifsc_code)
        editor.putString("uname",uname)
        editor.putString("pass",pass)
        editor.putString("total_present",total_present)
        editor.putString("total_absent",total_absent)
        editor.putString("total_late",total_late)
        editor.putString("total_permission",total_permission)
        editor.putString("absent_salary",absent_salary)
        editor.commit()
    }

    fun setLogout(id:String,name:String,mobile:String,email:String,
                city:String,state:String,img:String,fxd_slry:String,
                curr_salary:String,tot_present:String,address:String,
                acc_name:String,acc_no:String,bank_name:String,
                branch_name:String,ifsc_code:String){
        val editor = sharedPreferences.edit()
        editor.putString("id", "")
        editor.putString("name", "")
        editor.putString("mobile", "")
        editor.putString("email", "")
        editor.putString("city", "")
        editor.putString("state", "")
        editor.putString("image", "")
        editor.putString("address", "")
        editor.putString("fixed_salary", "")
        editor.putString("current_salary", "")
        editor.putString("total_present", "")
        editor.putString("acc_name","")
        editor.putString("acc_no","")
        editor.putString("bank_name","")
        editor.putString("branch_name","")
        editor.putString("ifsc_code","")
        editor.commit()
    }

    fun setVendor(typeid:String,vendor_id:String,vendor_nm:String,vendor_img:String) {
        val editor = sharedPreferences.edit()
        editor.putString("typeid", typeid)
        editor.putString("vendor_id", vendor_id)
        editor.putString("vendor_nm", vendor_nm)
        editor.putString("vendor_img", vendor_img)
        editor.commit()
    }

    fun setToken(t : String) {
        val editor = sharedPreferences.edit()
        editor.putString("token", t)
        editor.commit()
    }

    fun getToken():String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("token", "")
    }

    fun gettypeid():String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("typeid", "")
    }

    fun getvendor_id():String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("vendor_id", "")
    }

    fun getvendor_nm():String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("vendor_nm", "")
    }

    fun getvendor_img():String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("vendor_img", "")
    }

    fun getValue(str: String): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString(str, "")
    }

    fun setMaintenance(t : String) {
        val editor = sharedPreferences.edit()
        editor.putString("maintenance", t)
        editor.commit()
    }
    fun Maintenance():String?{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getString("maintenance", "")
    }

    fun setServiceState(b: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("service", b)
        editor.commit()
    }

    fun getServiceState(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context)
        return sharedPreferences.getBoolean("service", false)
    }
}
