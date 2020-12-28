package com.elancier.abnaattendance

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_bottom_navigation_light.*
import java.io.ByteArrayOutputStream
import java.lang.Exception


class MainActivity : AppCompatActivity(),ConnectivityReceiver.ConnectivityReceiverListener  {
    private val mTextMessage: TextView? = null
    private var navigation: BottomNavigationView? = null
    private val search_bar: View? = null
    val activity=this
    lateinit var pop:AlertDialog
    lateinit var popup:AlertDialog
    internal lateinit var byteArray: ByteArray
    lateinit var imageone:ImageView
    internal lateinit var imagecode: String
    var utils:Utils?=null
    var statusval=""
    var reason=""
    var fragmenthome:Homefragment?=null
    private var listener: mydataBack? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation_light)
        utils=Utils(this)
        init()
        loadFragment(Homefragment())
        fragmenthome=Homefragment()
        textView2.setText(utils!!.getname()!!.toUpperCase())

    }
    fun setListener(listener: mydataBack?) {
        this.listener = listener
    }

    fun cam(status:String){
        statusval=status
        val startCustomCameraIntent = Intent(this, CameraActivity::class.java)
        startActivityForResult(startCustomCameraIntent, 1)
    }
    fun init() {
        checkConnection()
        navigation = findViewById<View>(R.id.navigation) as BottomNavigationView
        navigation!!.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_recent -> {
                    //mTextMessage!!.text = item.title
                    fragment = Homefragment()
                    fragmenthome=fragment
                    return@OnNavigationItemSelectedListener loadFragment(fragment)
                }
                R.id.navigation_favorites -> {
                    //mTextMessage!!.text = item.title
                    fragment = Report_fragment()
                    frameLayout.visibility = View.GONE
                    return@OnNavigationItemSelectedListener loadFragment(fragment)
                }
                R.id.navigation_nearby -> {
                    fragment = Profilefragment()
                    return@OnNavigationItemSelectedListener loadFragment(fragment)
                }
            }
            false
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return

        if (requestCode == 1) {
            val photoUri = data!!.data

            Log.e("Photouri", photoUri.toString());
            var bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, photoUri)

            val dialogBuilder = AlertDialog.Builder(activity!!)
// ...Irrelevant code for customizing the buttons and title
            val inflater = activity!!.getLayoutInflater()
            val dialogView = inflater.inflate(R.layout.imagepopup, null)
            imageone=dialogView.findViewById<ImageView>(R.id.imageView);
            val textView10=dialogView.findViewById<TextView>(R.id.textView10)
            val reasons=dialogView.findViewById<EditText>(R.id.reason)

            imageone.setImageBitmap(bitmap);
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
            byteArray = stream.toByteArray()
            imagecode = Base64.encodeToString(byteArray, Base64.DEFAULT)
            imagecode = "data:image/png;base64,$imagecode"

            val retke=dialogView.findViewById<Button>(R.id.retke);
            val button=dialogView.findViewById<Button>(R.id.button);
            dialogBuilder.setView(dialogView)
            dialogBuilder.setCancelable(true)
            pop=dialogBuilder.create()
            pop.show()
            val Homefragment=Homefragment()
            println("lat" + Homefragment.lat)
            println("longi" + Homefragment.longi)

            if(statusval=="in"){
                textView10.setText("TIME IN")
                textView10.setBackgroundColor(resources.getColor(R.color.green_500))

            }
            else{
                textView10.setText("TIME OUT")
                textView10.setBackgroundColor(resources.getColor(R.color.red_400))

            }


            retke.setOnClickListener {
                val startCustomCameraIntent = Intent(activity, CameraActivity::class.java)
                startActivityForResult(startCustomCameraIntent, 1)

            }

            button.setOnClickListener {
               // gps!!.getLocation()

                val tsLong = System.currentTimeMillis() / 1000
                val ts = tsLong.toString()
                //var locations=lat.toString()+","+longi.toString()
                pop.dismiss()
                reason=reasons.text.toString().trim()
                try {
                    val firstFragment: Homefragment =
                            supportFragmentManager.fragments[0] as Homefragment
                    firstFragment!!.myActions()
                }
                catch (e:Exception){
                    //Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                    fragmenthome!!.myActions()
                }
                var idval="";
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }





    fun animateNavigation(hide: Boolean) {
        var isNavigationHide = false

        if (isNavigationHide && hide || !isNavigationHide && !hide) return
        isNavigationHide = hide
        val moveY = if (hide) 2 * navigation!!.height else 0
        navigation!!.animate().translationY(moveY.toFloat()).setStartDelay(100).setDuration(300)
            .start()
    }
    private fun checkConnection() {
        val isConnected = ConnectivityReceiver.isConnected()
        //showSnack(isConnected)
        if(isConnected){

        }
        else{
            startActivity(Intent(activity!!, NoItemInternetImage::class.java))

        }
    }

    override fun onResume() {
        super.onResume()
        MyApplication.getInstance().setConnectivityListener(this);
        ConnectivityReceiver.connectivityReceiverListener = this
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if(isConnected){

        }
        else{
            startActivity(Intent(activity!!, NoItemInternetImage::class.java))

        }
    }




}