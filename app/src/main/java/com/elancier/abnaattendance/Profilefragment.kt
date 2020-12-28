package com.elancier.abnaattendance

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.elancier.peace_admin.Appconstands
import com.mikhaellopez.circularimageview.CircularImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Homefragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profilefragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var prolin:LinearLayout?=null
    var utils:Utils?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        utils=Utils(activity!!)
        val view= inflater.inflate(R.layout.activity_profile_wallet, container, false)
        val image=view.findViewById<CircularImageView>(R.id.image)
        val namestaff=view.findViewById<TextView>(R.id.namestaff)
        val fixsalary=view.findViewById<TextView>(R.id.fixsalary)
        val presenttot=view.findViewById<TextView>(R.id.presenttot)
        val curslry=view.findViewById<TextView>(R.id.curslry)
        val banklin=view.findViewById<LinearLayout>(R.id.banklin)
        prolin=view.findViewById(R.id.prolin)

        val version=view.findViewById<TextView>(R.id.version)
        val logout=view.findViewById<LinearLayout>(R.id.logout)

        logout.setOnClickListener {
            val alert=AlertDialog.Builder(activity!!)
            alert.setTitle("Logout?")
            alert.setMessage("Are you sure want to logout?")
            alert.setPositiveButton("Yes",DialogInterface.OnClickListener { dialogInterface, i ->
                utils!!.setLogout("","","","","","",
                    "","","","","","",""
                    ,"","","")
                utils!!.setLogin(false)
                startActivity(Intent(activity!!,LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                activity!!.finish()
            })
            alert.setNegativeButton("No",DialogInterface.OnClickListener { dialogInterface, i ->
               dialogInterface.cancel()
            })
            val pop=alert.create()
            pop.show()

        }

        namestaff!!.setText(utils!!.getname())
        fixsalary!!.setText(Appconstands.rupees+utils!!.getfixed_salary())
        presenttot!!.setText(utils!!.gettotal_present())
        curslry!!.setText(Appconstands.rupees+utils!!.getcurrent_salary())
        println("image"+utils!!.getimage())
        Glide.with(activity!!).load(utils!!.getimage()).into(image)
        version.setText("Version "+BuildConfig.VERSION_NAME)

        prolin!!.setOnClickListener {
        startActivity(Intent(activity,General_Info::class.java))
        }
        banklin.setOnClickListener {
            startActivity(Intent(activity,Bank_Details::class.java))
        }


        return view
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
        fun newInstance(param1: String, param2: String) =
            Homefragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}