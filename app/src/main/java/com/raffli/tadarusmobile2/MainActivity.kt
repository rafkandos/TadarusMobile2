package com.raffli.tadarusmobile2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_rekap_tadarus.*

class MainActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    val db = FirebaseDatabase.getInstance()
    private var tadarusData = ArrayList<tadarus_data>()
    private var lastTadarus = tadarus_data()
    private val RC_SIGN_IN = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTambahCatatan.setOnClickListener() {
            intent = Intent(applicationContext, InsertTadarusActivity::class.java)
            startActivity(intent)
        }

        btnRekapCatatan.setOnClickListener() {
            intent = Intent(applicationContext, RekapTadarusActivity::class.java)
            startActivity(intent)
        }

        fabLogout.setOnClickListener() {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {
                        Toast.makeText(this@MainActivity, "Logout Berhasil", Toast.LENGTH_SHORT).show()
                                intent = Intent(applicationContext,
                            LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                })
        }

        auth = FirebaseAuth.getInstance()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = db.getReference()
        getReference.child("Admin").child(getUserID).child("Tadarus")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val item = snapshot.getValue(tadarus_data::class.java)
                            item?.key = snapshot.key
                            tadarusData.add(item!!)
                        }
                        tadarusData.reverse()
                        lastTadarus = tadarusData.first()

                        txtJuz.setText("Juz: " + lastTadarus.juz)
                        txtSurah.setText("Surat: " + lastTadarus.surat)
                        txtAyat.setText("Ayat terakhir: " + lastTadarus.ayat)
                        txtTempat.setText("Tempat tadarus: " + lastTadarus.tempat)
                    }
                    else {
                        txtJuz.visibility = View.GONE
                        txtSurah.visibility = View.GONE
                        txtAyat.visibility = View.GONE
                        txtTempat.visibility = View.GONE
                        txtNoData.visibility = View.VISIBLE
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Data failed to load", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }
}